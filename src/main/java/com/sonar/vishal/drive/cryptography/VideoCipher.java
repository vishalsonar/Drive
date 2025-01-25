package com.sonar.vishal.drive.cryptography;

import com.sonar.vishal.drive.component.Notification;
import com.sonar.vishal.drive.util.Constant;
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

    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    @Value("${drive.video.encryption.salt}")
    private String hexSalt;

    @Value("${drive.video.encryption.password}")
    private String password;

    @Value("${drive.video.encryption.initialization.vector}")
    private String initializationVector;

    @Autowired
    private Notification notification;

    public void init() {
        try {
            if (encryptionCipher == null && decryptionCipher == null) {
                byte[] salt = new BigInteger(hexSalt, 16).toByteArray();
                KeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
                byte[] key = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
                SecretKey secretKey = new SecretKeySpec(key, AES);
                byte[] initializationVectorByte = new BigInteger(initializationVector, 16).toByteArray();
                GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initializationVectorByte);

                encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
                decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
                encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
                decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            }
        } catch (Exception exception) {
            encryptionCipher = null;
            decryptionCipher = null;
            notification.updateUI(Constant.FAILED_TO_INITIALIZE_APPLICATION, true);
        }
    }

    public Cipher getEncryptionCipher() {
        return encryptionCipher;
    }

    public Cipher getDecryptionCipher() {
        return decryptionCipher;
    }
}
