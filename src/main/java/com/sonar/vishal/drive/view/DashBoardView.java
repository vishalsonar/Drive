package com.sonar.vishal.drive.view;

import com.sonar.vishal.drive.component.Folder;
import com.sonar.vishal.drive.component.HR;
import com.sonar.vishal.drive.component.Logo;
import com.sonar.vishal.drive.component.PasswordDialog;
import com.sonar.vishal.drive.context.Context;
import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(Constant.EMPTY)
public class DashBoardView extends VerticalLayout {

    public DashBoardView() {
        setWidthFull();
        setHeightFull();
        add(Context.getBean(Logo.class));
        add(Context.getBean(HR.class));
        add(Context.getBean(Folder.class).open());
        add(Context.getBean(PasswordDialog.class).openDialog());
    }
}
