package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Tag(Constant.VIDEO)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Video extends HtmlContainer {

    private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors.attributeWithDefault(Constant.SRC, Constant.EMPTY);

    public String getSrc() {
        return get(srcDescriptor);
    }

    public void setSrc(String src) {
        setStyle();
        set(srcDescriptor, src);
    }

    public void setStyle() {
        getElement().setProperty(Constant.CONTROLS, true);
        getElement().setProperty(Constant.WIDTH, Constant.THREE_ZERO_ZERO);
        getElement().setProperty(Constant.HEIGHT, Constant.FOUR_ZERO_ZERO);
    }
}
