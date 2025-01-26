package com.sonar.vishal.drive.cryptography;

import com.sonar.vishal.drive.context.Context;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.spec.KeySpec;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VideoCipher {

    private static final String AES = "AES";
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";

    @Value("${drive.video.encryption.salt}")
    private String hexSalt;

    @Value("${drive.video.encryption.password}")
    private String password;

    @Value("${drive.video.encryption.initialization.vector}")
    private String initializationVector;

    @Autowired
    private Logger logger;

    private SecretKey secretKey;
    private byte[] initializationVectorByte;

    private Cipher initialize(boolean isEncrypt) {
        Cipher cipher = null;
        try {
            if (secretKey == null && initializationVectorByte == null) {
                byte[] salt = Context.getBean(BigInteger.class, hexSalt, 16).toByteArray();
                KeySpec pbeKeySpec = Context.getBean(PBEKeySpec.class, password.toCharArray(), salt);
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
                byte[] key = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
                secretKey = Context.getBean(SecretKeySpec.class, key, AES);
                initializationVectorByte = Context.getBean(BigInteger.class, initializationVector, 16).toByteArray();
            }
            GCMParameterSpec gcmParameterSpec = Context.getBean(GCMParameterSpec.class, 128, initializationVectorByte);
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        } catch (Exception exception) {
            logger.error("Failed to initialize cipher", exception);
        }
        return cipher;
    }

    public Cipher getEncryptionCipher() {
        return initialize(true);
    }

    public Cipher getDecryptionCipher() {
        return initialize(false);
    }
}
