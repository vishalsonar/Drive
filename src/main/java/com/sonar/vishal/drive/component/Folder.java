package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Folder extends HorizontalLayout {

    @Value("${drive.root.folder}")
    private String rootFolder;

    @Autowired
    private transient Logger logger;

    @Autowired
    private Space space;

    @Autowired
    private Notification notification;

    @Autowired
    private NewFolderButton newFolderButton;

    public Folder open() {
        try {
            setWidthFull();
            setHeightFull();
            setMargin(true);
            setPadding(true);
            add(newFolderButton.updateUI());
            getStyle().setFlexWrap(Style.FlexWrap.WRAP);

            if (Files.isDirectory(Path.of(rootFolder))) {
                Files.list(Path.of(rootFolder)).toList().stream().sorted()
                        .filter(file -> !file.getFileName().toString().contains(Constant.DOT_DS_STORE))
                        .map(path -> Context.getBean(FolderButton.class).updateUI(path))
                        .forEach(this::add);
            }
            add(space);
        } catch (Exception exception) {
            notification.updateUI(Constant.ERROR_LOADING_PAGE, true);
            logger.error("Failed to load folder :: " + rootFolder, exception);
        }
        return this;
    }
}
