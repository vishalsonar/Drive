package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.cryptography.VideoCipher;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.CipherInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoDialog extends Dialog {

    private UI ui;
    private boolean isDirty;

    @Autowired
    private Video video;

    @Autowired
    private transient Logger logger;

    @Autowired
    private ProgressBar progressBar;

    @Autowired
    private DialogButton cancelButton;

    @Autowired
    private NativeLabel progressBarLabel;

    @Autowired
    private Notification notification;

    @Autowired
    private transient VideoCipher videoCipher;

    public VideoDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        addDetachListener(detachEvent -> ui = null);
        addAttachListener(attachEvent -> ui = attachEvent.getUI());
    }

    public void open(Path path) {
        if (!isDirty) {
            isDirty = true;
            progressBarLabel.setText(Constant.PROCESSING_VIDEO);
            add(progressBarLabel, progressBar);
            convertToVideo(path);
            cancelButton.updateUI(Constant.BACK, VaadinIcon.ARROW_CIRCLE_LEFT, event -> close());
            cancelButton.setWidth(60, Unit.PERCENTAGE);
            getFooter().add(cancelButton);
        }
        super.open();
    }

    public void convertToVideo(Path path) {
        CompletableFuture.supplyAsync(() -> {
            StringBuilder videoString = Context.getBean(StringBuilder.class);
            videoString.append(Constant.VIDEO_BASE_64_STRING);
            try (FileInputStream fileInputStream = Context.getBean(FileInputStream.class, Context.getBean(File.class, path.toString()));
                 CipherInputStream cipherInputStream = Context.getBean(CipherInputStream.class, fileInputStream, videoCipher.getDecryptionCipher())) {
                videoString.append(Base64.getEncoder().encodeToString(cipherInputStream.readAllBytes()));
            } catch (Exception exception) {
                ui.access(() -> {
                    notification.updateUI(Constant.VIDEO_LOAD_FAILED, true);
                    super.close();
                });
                logger.error("Failed to decrypt video", exception);
            }
            video.setSrc(videoString.toString());
            ui.access(() -> {
                removeAll();
                add(video);
                cancelButton.setWidth(30, Unit.PERCENTAGE);
            });
            return Constant.EMPTY;
        });
    }
}
