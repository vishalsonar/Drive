package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FolderDialog extends Dialog {

    private boolean isDirty;

    @Autowired
    private Space space;

    @Autowired
    private DialogButton cancelButton;

    @Autowired
    private VideoUpload videoUpload;

    @Autowired
    private Notification notification;

    public void updateUI(Path path) {
        setWidthFull();
        setHeightFull();
        if (!isDirty) {
            isDirty = true;
            setHeaderTitle(path.getFileName().toString());
            loadVideoButtonList(path);
            cancelButton.updateUI(Constant.BACK, VaadinIcon.ARROW_CIRCLE_LEFT, event -> close());
            cancelButton.setWidth(15, Unit.PERCENTAGE);
            getFooter().add(cancelButton);
        }
        open();
    }

    private void loadVideoButtonList(Path path) {
        try {
            HorizontalLayout horizontalLayout = Context.getBeansOfType(HorizontalLayout.class).get(Constant.GET_HORIZONTAL_LAYOUT);
            horizontalLayout.add(videoUpload.updateUI(path));
            Files.walk(Paths.get(path.toString()), FileVisitOption.FOLLOW_LINKS)
                    .filter(file -> !file.toFile().isDirectory())
                    .filter(file -> !file.getFileName().toString().contains(Constant.DOT_DS_STORE))
                    .map(file -> Context.getBean(VideoButton.class, file))
                    .forEach(horizontalLayout::add);
            horizontalLayout.add(space);
            horizontalLayout.setWidthFull();
            horizontalLayout.setHeightFull();
            horizontalLayout.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
            add(horizontalLayout);
        } catch (Exception exception) {
            notification.updateUI(Constant.VIDEO_LOAD_FAILED, true);
        }
    }
}
