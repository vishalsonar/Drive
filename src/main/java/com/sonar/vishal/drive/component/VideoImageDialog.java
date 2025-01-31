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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.CipherInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoImageDialog extends Dialog {

    private UI ui;
    private boolean isOpen;
    private boolean isDirty;
    private String tempVideoPath;

    @Value("${drive.stream.folder}")
    private String streamFolder;

    @Autowired
    private Image image;

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

    public VideoImageDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        addDetachListener(detachEvent -> ui = null);
        addAttachListener(attachEvent -> ui = attachEvent.getUI());
    }

    public void open(Path path) {
        if (!isDirty) {
            isDirty = true;
            progressBarLabel.setText(Constant.PROCESSING_VIDEO_IMAGE);
            add(progressBarLabel, progressBar);
            convertToVideoImage(path);
            cancelButton.updateUI(Constant.BACK, VaadinIcon.ARROW_CIRCLE_LEFT, event -> close());
            cancelButton.setWidth(60, Unit.PERCENTAGE);
            addDetachListener(detachEvent -> close());
            getFooter().add(cancelButton);
        }
        isOpen = true;
        super.open();
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            deleteFile();
            super.close();
        }
    }

    private void convertToVideoImage(Path path) {
        CompletableFuture.supplyAsync(() -> {
            final boolean isImage = Pattern.matches(Constant.IMAGE_NAME_PATTERN, path.getFileName().toString());
            if (isImage) {
                pushImage(path);
            } else {
                pushVideo(path);
            }
            return Constant.EMPTY;
        });
    }

    private void pushImage(Path path) {
        StringBuilder imageString = Context.getBean(StringBuilder.class);
        imageString.append(Constant.IMAGE_BASE_64_STRING);
        try (FileInputStream fileInputStream = Context.getBean(FileInputStream.class, Context.getBean(File.class, path.toString()));
             CipherInputStream cipherInputStream = Context.getBean(CipherInputStream.class, fileInputStream, videoCipher.getDecryptionCipher())) {
            imageString.append(Base64.getEncoder().encodeToString(cipherInputStream.readAllBytes()));
            image.setSrc(imageString.toString());
            pushComponent(image);
        } catch (Exception exception) {
            pushException(Constant.IMAGE_LOAD_FAILED, "Failed to decrypt image", exception);
        }
    }

    private void pushVideo(Path path) {
        StringBuilder videoPath = Context.getBean(StringBuilder.class);
        videoPath.append(streamFolder);
        videoPath.append(Constant.FOLDER_SLASH);
        videoPath.append(path.getFileName().toString());
        tempVideoPath = videoPath.toString();
        try (FileInputStream fileInputStream = Context.getBean(FileInputStream.class, Context.getBean(File.class, path.toString()));
             FileOutputStream fileOutputStream = Context.getBean(FileOutputStream.class, Context.getBean(File.class, tempVideoPath));
             CipherInputStream cipherInputStream = Context.getBean(CipherInputStream.class, fileInputStream, videoCipher.getDecryptionCipher())) {
            cipherInputStream.transferTo(fileOutputStream);
            video.setSrc(Constant.VIDEO_HOST_PATH + path.getFileName().toString());
            pushComponent(video);
        } catch (Exception exception) {
            pushException(Constant.VIDEO_LOAD_FAILED, "Failed to decrypt video", exception);
            deleteFile();
        }
    }

    private void deleteFile() {
        if (tempVideoPath != null) {
            try {
                Files.deleteIfExists(Context.getBean(File.class, tempVideoPath).toPath());
            } catch (IOException ioException) {
                logger.error("Filed to delete file", ioException);
            }
        }
    }

    private void pushComponent(com.vaadin.flow.component.Component component) {
        ui.access(() -> {
            removeAll();
            add(component);
            cancelButton.setWidth(30, Unit.PERCENTAGE);
        });
    }

    private void pushException(String notificationMessage, String logMessage, Exception exception) {
        ui.access(() -> {
            notification.updateUI(notificationMessage, true);
            super.close();
        });
        logger.error(logMessage, exception);
    }
}
