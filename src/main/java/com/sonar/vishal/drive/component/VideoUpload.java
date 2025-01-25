package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.cryptography.VideoCipher;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.CipherOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoUpload extends Upload {

    @Autowired
    private VideoCipher videoCipher;

    @Autowired
    private Notification notification;

    public VideoUpload() {
        setDropAllowed(true);
        setWidthFull();
        setMaxFiles(1);
        setMaxFileSize(100 * 1024 * 1024);
        setAcceptedFileTypes(Constant.ACCEPTED_FILE_TYPE);
    }

    public VideoUpload updateUI(Path path) {
        MultiFileMemoryBuffer fileMemoryBuffer = Context.getBean(MultiFileMemoryBuffer.class);
        setReceiver(fileMemoryBuffer);
        addSucceededListener(event -> {
            notification.updateUI(Constant.VIDEO_UPLOAD_SUCCESS_MESSAGE, false);
            transferFile(fileMemoryBuffer, event, path);
        });
        addFileRejectedListener(event -> notification.updateUI(Constant.VIDEO_UPLOAD_FAILED_MESSAGE, true));
        addFailedListener(failedEvent -> notification.updateUI(Constant.VIDEO_UPLOAD_FAILED_MESSAGE, true));
        return this;
    }

    public void transferFile(MultiFileMemoryBuffer fileMemoryBuffer, SucceededEvent event, Path path) {
        File newFile = Context.getBean(File.class, path.toString() + Constant.FOLDER_SLASH + event.getFileName());
        if (newFile.exists()) {
            notification.updateUI(Constant.FILE_CREATE_EXIST_MESSAGE, true);
            return;
        }
        try (FileOutputStream fileOutputStream = Context.getBean(FileOutputStream.class, newFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, videoCipher.getEncryptionCipher())) {
            fileMemoryBuffer.getInputStream(event.getFileName()).transferTo(cipherOutputStream);
        } catch (Exception exception) {
            notification.updateUI(Constant.VIDEO_UPLOAD_FAILED_MESSAGE, true);
        }
    }
}
