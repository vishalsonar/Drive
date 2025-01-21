package com.sonar.vishal.drive.configuration;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MultiFileMemoryBuffer getMultiFileMemoryBuffer() {
        return new MultiFileMemoryBuffer();
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HorizontalLayout getHorizontalLayout() {
        return new HorizontalLayout();
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public StringBuilder getStringBuilder() {
        return new StringBuilder();
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FileOutputStream getFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public File getFile(String path) {
        return new File(path);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ProgressBar getProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        return progressBar;
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NativeLabel getNativeLabel() {
        return new NativeLabel();
    }

//    @Bean
//    @UIScope
//    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//
//    @Bean
//    @UIScope
//    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
}
