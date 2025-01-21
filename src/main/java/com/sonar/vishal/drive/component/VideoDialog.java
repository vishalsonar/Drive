package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
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
    private ProgressBar progressBar;

    @Autowired
    private NativeLabel progressBarLabel;

    @Autowired
    private Notification notification;

    public VideoDialog() {
        addDetachListener(detachEvent -> ui = null);
        addAttachListener(attachEvent -> ui = attachEvent.getUI());
    }

    public void open(Path path) {
        if (!isDirty) {
            isDirty = true;
            progressBarLabel.setText(Constant.PROCESSING_VIDEO);
            add(progressBarLabel, progressBar);
            convertToVideo(path);
        }
        super.open();
    }

    public void convertToVideo(Path path) {
        CompletableFuture.supplyAsync(() -> {
            StringBuilder videoString = Context.getBean(StringBuilder.class);
            videoString.append(Constant.VIDEO_BASE_64_STRING);
            try {
                videoString.append(Base64.getEncoder().encodeToString(Files.readAllBytes(path)));
            } catch (Exception exception) {
                ui.access(() -> notification.updateUI(Constant.VIDEO_LOAD_FAILED, true));
            }
            video.setSrc(videoString.toString());
            ui.access(() -> {
                removeAll();
                add(video);
            });
            return Constant.EMPTY;
        });
    }
}
