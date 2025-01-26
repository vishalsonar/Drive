package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FolderDialog extends Dialog {

    private boolean isDirty;

    @Autowired
    private Space space;

    @Autowired
    private transient Logger logger;

    @Autowired
    private DialogButton cancelButton;

    @Autowired
    private VideoUpload videoUpload;

    @Autowired
    private Notification notification;

    public FolderDialog() {
        setWidthFull();
        setHeightFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    public void updateUI(Path path) {
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
        try (Stream<Path> pathStream = Files.walk(Paths.get(path.toString()), FileVisitOption.FOLLOW_LINKS)) {
            HorizontalLayout horizontalLayout = Context.getBeansOfType(HorizontalLayout.class).get(Constant.GET_HORIZONTAL_LAYOUT);
            horizontalLayout.add(videoUpload.updateUI(path));
            pathStream.filter(file -> !file.toFile().isDirectory())
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
            logger.error("Failed to load files :: " + path.toString(), exception);
        }
    }
}
