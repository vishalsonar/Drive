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
public class DropLabelSpan extends Span {

    public DropLabelSpan() {
        setText(Constant.LABEL_DROP_HERE);
    }
}
