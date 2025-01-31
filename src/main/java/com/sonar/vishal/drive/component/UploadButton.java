package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadButton extends Button {

    public UploadButton() {
        setText(Constant.BUTTON_DROP_HERE);
    }
}
