package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewFolderButton extends Button {

    @Autowired
    private NewFolderDialog newFolderDialog;

    public NewFolderButton updateUI() {
        setText(Constant.NEW_FOLDER);
        setIcon(VaadinIcon.FOLDER_ADD.create());
        setWidth(15, Unit.PERCENTAGE);
        setHeight(20, Unit.PERCENTAGE);
        addClickListener(event -> newFolderDialog.open());
        return this;
    }
}
