package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Logo extends Span {

    public Logo() {
        setText(Constant.DRIVE);
        getStyle().set(Constant.WIDTH, Constant.ONE_ZERO_ZERO_PERCENTAGE);
        getStyle().set(Constant.FONT_SIZE, Constant.FOUR_ZERO_PX);
        getStyle().set(Constant.TEXT_ALIGN, Constant.CENTER);
    }
}
