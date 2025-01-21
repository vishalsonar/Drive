package com.sonar.vishal.drive;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;

@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@Theme("my-theme")
@Push(value = PushMode.AUTOMATIC)
public class DriveAppShell implements AppShellConfigurator {
}
