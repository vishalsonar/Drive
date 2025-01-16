package com.sonar.vishal.drive.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogButton extends Button {

    public DialogButton updateUI(String name, VaadinIcon icon, Consumer<ClickEvent<Button>> clickEventConsumer) {
        setText(name);
        setIcon(icon.create());
        setWidth(30, Unit.PERCENTAGE);
        setHeight(90, Unit.PERCENTAGE);
        addClickListener(event -> clickEventConsumer.accept(event));
        return this;
    }
}
