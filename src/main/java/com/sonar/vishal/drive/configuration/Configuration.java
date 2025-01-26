package com.sonar.vishal.drive.configuration;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger getLogger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return LoggerFactory.getLogger(classOnWired);
    }

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

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CipherOutputStream getCipherOutputStream(FileOutputStream fileOutputStream, Cipher cipher) {
        return new CipherOutputStream(fileOutputStream, cipher);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CipherInputStream getCipherInputStream(FileInputStream fileInputStream, Cipher cipher) {
        return new CipherInputStream(fileInputStream, cipher);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BigInteger getBigInteger(String val, int radix) {
        return new BigInteger(val, radix);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PBEKeySpec getPBEKeySpec(char[] password, byte[] salt) {
        return new PBEKeySpec(password, salt, 65536, 256);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SecretKeySpec getSecretKeySpec(byte[] key, String algorithm) {
        return new SecretKeySpec(key, algorithm);
    }

    @Bean
    @UIScope
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GCMParameterSpec getGCMParameterSpec(int tLen, byte[] src) {
        return new GCMParameterSpec(tLen, src);
    }
}
