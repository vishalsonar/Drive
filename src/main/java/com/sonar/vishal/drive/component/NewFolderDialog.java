package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewFolderDialog extends Dialog {

    private UI ui;
    private boolean isDirty;

    @Value("${spring.drive.root.folder}")
    private String rootFolder;

    @Autowired
    private transient Logger logger;

    @Autowired
    private DialogButton createButton;

    @Autowired
    private DialogButton cancelButton;

    @Autowired
    private Notification notification;

    @Autowired
    private DialogTextField dialogTextField;

    public NewFolderDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setWidth(40, Unit.PERCENTAGE);
        setHeaderTitle(Constant.CREATE_NEW_FOLDER);
        addDetachListener(detachEvent -> ui = null);
        addAttachListener(attachEvent -> ui = attachEvent.getUI());
    }

    @Override
    public void open() {
        if (!isDirty) {
            isDirty = true;
            dialogTextField.updateUI(Constant.FOLDER_NAME);
            dialogTextField.addKeyPressListener(keyPressEvent -> {
                if (Key.ENTER.matches(keyPressEvent.getKey().toString())) createButton.click();
            });
            createButton.updateUI(Constant.CREATE, VaadinIcon.CLOUD_DOWNLOAD, event -> createFolder());
            cancelButton.updateUI(Constant.CANCEL, VaadinIcon.ARROW_CIRCLE_LEFT, event -> close());
            addDialogCloseActionListener(dialogCloseActionEvent -> close());
            getFooter().add(createButton, cancelButton);
            add(dialogTextField);
        }
        dialogTextField.focus();
        super.open();
    }

    @Override
    public void close() {
        dialogTextField.setValue(Constant.EMPTY);
        super.close();
    }

    private void createFolder() {
        try {
            if (Files.isDirectory(Path.of(rootFolder))) {
                Path newFolder = Path.of(rootFolder + Constant.FOLDER_SLASH + dialogTextField.getValue());
                if (!Pattern.matches(Constant.FOLDER_NAME_PATTERN, dialogTextField.getValue())) {
                    Context.getBean(Notification.class).updateUI(Constant.INVALID_FILE_NAME, true);
                } else if (Files.exists(newFolder)) {
                    Context.getBean(Notification.class).updateUI(Constant.FOLDER_CREATE_EXIST_MESSAGE, true);
                } else {
                    Files.createDirectory(newFolder);
                    pushFolder(newFolder);
                }
            }
        } catch (Exception exception) {
            notification.updateUI(Constant.FOLDER_CREATE_FAILED_MESSAGE, true);
            logger.error("Failed to create folder", exception);
        }
    }

    private void pushFolder(Path path) {
        this.ui.getCurrentView().getChildren().toList().stream().filter(Folder.class::isInstance).findFirst().ifPresent(component -> {
            HorizontalLayout horizontalLayout = (HorizontalLayout) component;
            this.ui.access(() -> horizontalLayout.addComponentAtIndex(1, Context.getBean(FolderButton.class).updateUI(path)));
            Context.getBean(Notification.class).updateUI(Constant.FOLDER_CREATE_SUCCESS_MESSAGE, false);
            this.close();
        });
    }
}
