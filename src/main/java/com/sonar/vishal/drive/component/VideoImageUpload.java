package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.cryptography.VideoCipher;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.CipherOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoImageUpload extends Upload {

    @Autowired
    private transient Logger logger;

    @Autowired
    private transient VideoCipher videoCipher;

    @Autowired
    private Notification notification;

    public VideoImageUpload() {
        setWidthFull();
        setMaxFiles(1);
        setDropAllowed(true);
        setMaxFileSize(500 * 1024 * 1024);
        setHeight(25, Unit.PERCENTAGE);
        setDropLabel(Context.getBean(DropLabelSpan.class));
        setUploadButton(Context.getBean(UploadButton.class));
        setAcceptedFileTypes(Constant.ACCEPTED_VIDEO_FILE_TYPE, Constant.ACCEPTED_IMAGE_FILE_TYPE);
    }

    public VideoImageUpload updateUI(Path path) {
        MultiFileMemoryBuffer fileMemoryBuffer = Context.getBean(MultiFileMemoryBuffer.class);
        setReceiver(fileMemoryBuffer);
        addSucceededListener(event -> transferFile(fileMemoryBuffer, event, path));
        addFileRejectedListener(fileRejectedEvent -> notification.updateUI(Constant.FILE_REJECT_ERROR_MESSAGE, true));
        return this;
    }

    public void transferFile(MultiFileMemoryBuffer fileMemoryBuffer, SucceededEvent event, Path path) {
        File newFile = Context.getBean(File.class, path.toString() + Constant.FOLDER_SLASH + event.getFileName());
        if (newFile.exists()) {
            notification.updateUI(Constant.FILE_CREATE_EXIST_MESSAGE, true);
        } else {
            transferFile(fileMemoryBuffer, event, newFile);
        }
    }

    private void transferFile(MultiFileMemoryBuffer fileMemoryBuffer, SucceededEvent event, File file) {
        try (FileOutputStream fileOutputStream = Context.getBean(FileOutputStream.class, file);
             CipherOutputStream cipherOutputStream = Context.getBean(CipherOutputStream.class, fileOutputStream, videoCipher.getEncryptionCipher())) {
            fileMemoryBuffer.getInputStream(file.getName()).transferTo(cipherOutputStream);
            notification.updateUI(Constant.VIDEO_IMAGE_UPLOAD_SUCCESS_MESSAGE, false);
            pushVideoImageButton(file, event);
        } catch (Exception exception) {
            deleteFile(file);
            notification.updateUI(Constant.VIDEO_IMAGE_UPLOAD_FAILED_MESSAGE, true);
            logger.error("Failed to encrypt video/image", exception);
        }
    }

    private void pushVideoImageButton(File file, SucceededEvent event) {
        event.getSource().getParent().ifPresent(component -> {
            HorizontalLayout horizontalLayout = (HorizontalLayout) component;
            component.getUI().get().access(() -> horizontalLayout.addComponentAtIndex(1, Context.getBean(VideoButton.class, file.toPath())));
        });
    }

    private void deleteFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException ioException) {
            logger.error("Filed to delete file", ioException);
        }
    }
}
