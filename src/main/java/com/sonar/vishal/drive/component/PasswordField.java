package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PasswordField extends com.vaadin.flow.component.textfield.PasswordField {

    public PasswordField() {
        setWidthFull();
        setMaxLength(6);
        setAutofocus(true);
        setClearButtonVisible(true);
        setPrefixComponent(VaadinIcon.LOCK.create());
        setLabel(Constant.PASSWORD_LABEL);
        setHelperText(Constant.PASSWORD_HELPER_TEXT);
    }
}
