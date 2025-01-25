package com.sonar.vishal.drive.convert;

import com.vaadin.flow.spring.annotation.UIScope;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@UIScope
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HybridVideoEncryption {

//    static {
//        Security.addProvider(new BouncyCastleProvider());
//        Security.addProvider(new BouncyCastlePQCProvider());
//    }

    @Value("${drive.video.encryption.initialization.vector}")
    private static String initializationVector = "6266C41C5632D08BC14A1F71";

    @Value("${drive.video.encryption.salt}")
    private static String hexSalt = "5EAE55EA1DDA9CED973AA23668658A9A";

    public static void main(String[] a) throws Exception {
        String inputFilePath = "/Users/vishalsonar/Downloads/Project/Spring/DriveTest/MAY/2024-11-06 17.04.51.mp4";
        String encryptedFilePath = "/Users/vishalsonar/Downloads/Project/Spring/DriveTest/MAY/2024-11-06 17.04.51.mp4.enc";
        String decryptedFilePath = "/Users/vishalsonar/Downloads/Project/Spring/DriveTest/MAY/decrypted_video.mp4";

        // Replace these with actual file paths
        File inputFile = new File(inputFilePath);
        File encryptedFile = new File(encryptedFilePath);
        File decryptedFile = new File(decryptedFilePath);

        // Create an instance of SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        // Create an array to hold 12 random bytes
        byte[] randomBytes = new byte[32];
        // Fill the byte array with random values
        secureRandom.nextBytes(randomBytes);
        System.out.println(byteArrayToHex(randomBytes));

//        byte[] salt = new BigInteger(hexSalt, 16).toByteArray();
//
//        // AES encryption
//        SecretKey aesSecretKey = generateAESKeyFromPassword("StrongPassword", salt);
//        encrypt(inputFile, encryptedFile, aesSecretKey);
//        decrypt(encryptedFile, decryptedFile, aesSecretKey);
    }

    public static String byteArrayToHex(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString().toUpperCase();
    }

    public static SecretKey generateAESKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }

    public static void encrypt(File inputFile, File encryptedFile, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] initializationVectorByte = new BigInteger(initializationVector, 16).toByteArray();
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initializationVectorByte);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(encryptedFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void decrypt(File encryptedFile, File decryptedFile, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] initializationVectorByte = new BigInteger(initializationVector, 16).toByteArray();
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initializationVectorByte);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        try (FileInputStream fis = new FileInputStream(encryptedFile);
             FileOutputStream fos = new FileOutputStream(decryptedFile);
             CipherInputStream cis = new CipherInputStream(fis, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

}
