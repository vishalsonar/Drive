package com.sonar.vishal.drive.component;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notification extends com.vaadin.flow.component.notification.Notification {

    public void updateUI(String message, boolean isErrorNotification) {
        removeThemeNames(NotificationVariant.LUMO_ERROR.getVariantName(), NotificationVariant.LUMO_PRIMARY.getVariantName());
        addThemeVariants(isErrorNotification ? NotificationVariant.LUMO_ERROR : NotificationVariant.LUMO_PRIMARY);
        setPosition(Notification.Position.TOP_END);
        setDuration(4000);
        setText(message);
        open();
    }
}
