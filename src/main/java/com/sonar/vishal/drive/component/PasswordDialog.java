package com.sonar.vishal.drive.component;

import com.sonar.vishal.drive.util.Constant;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PasswordDialog extends Dialog {

    private transient MessageDigest digest;

    @Value("${drive.password.hash}")
    private String passwordHash;

    @Autowired
    private transient Logger logger;

    @Autowired
    private Notification notification;

    @Autowired
    private PasswordField passwordField;

    public PasswordDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setWidth(40, Unit.PERCENTAGE);
        initializeDigest();
    }

    public PasswordDialog openDialog() {
        passwordField.addKeyPressListener(this::processEvent);
        add(passwordField);
        open();
        return this;
    }

    private void initializeDigest() {
        try {
            digest = MessageDigest.getInstance(Constant.SHA_256);
        } catch (Exception exception) {
            logger.error("MessageDigest initialization failed.", exception);
        }
    }

    private void processEvent(KeyPressEvent keyPressEvent) {
        if (Key.ENTER.matches(keyPressEvent.getKey().toString())) {
            if (bytesToHex(digest.digest(passwordField.getValue().getBytes())).equals(passwordHash)) close();
            else notification.updateUI(Constant.PASSWORD_INVALID_ERROR, true);
        }
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
