package com.sonar.vishal.drive.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FolderButton extends Button {

    public FolderButton updateUI(String text) {
        setText(text);
        setIcon(VaadinIcon.FOLDER.create());
        setWidth(15, Unit.PERCENTAGE);
        setHeight(20, Unit.PERCENTAGE);
        return this;
    }
}
