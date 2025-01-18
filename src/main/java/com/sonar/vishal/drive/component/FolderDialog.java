package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FolderDialog extends Dialog {

    @Autowired
    private DialogButton cancelButton;

    @Autowired
    private VideoUpload videoUpload;

    public void updateUI(Path path) {
        setWidthFull();
        setHeightFull();
        setHeaderTitle(path.getFileName().toString());
        add(videoUpload.updateUI(path));
        cancelButton.updateUI(Constant.BACK, VaadinIcon.ARROW_CIRCLE_LEFT, event -> close());
        cancelButton.setWidth(15, Unit.PERCENTAGE);
        getFooter().add(cancelButton);
        open();
    }
}
