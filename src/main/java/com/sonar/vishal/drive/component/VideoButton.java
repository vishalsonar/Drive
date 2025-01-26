package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoButton extends Button {

    public VideoButton(Path path) {
        setText(path.getFileName().toString());
        setIcon(VaadinIcon.FILE_MOVIE.create());
        setWidth(15, Unit.PERCENTAGE);
        setHeight(15, Unit.PERCENTAGE);
        addClickListener(event -> Context.getBean(VideoDialog.class).open(path));
    }
}
