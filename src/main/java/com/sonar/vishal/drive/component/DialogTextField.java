package com.sonar.vishal.drive.component;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogTextField extends TextField {

    public void updateUI(String label) {
        setWidthFull();
        setMaxLength(100);
        setAutofocus(true);
        setLabel(label);
    }
}
