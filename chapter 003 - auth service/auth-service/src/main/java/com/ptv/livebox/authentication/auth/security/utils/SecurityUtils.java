package com.ptv.livebox.authentication.auth.security.utils;

import com.ptv.livebox.authentication.auth.security.AuthException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateException;
import java.util.Base64;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public interface SecurityUtils {
    int ERROR_INVALID_BASE64_VALUE = 5000;

    static KeyPair generateKeyPari(int length) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(length);
        return keyGen.generateKeyPair();
    }

    static Cipher createCipher(Key key, int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(mode, key);
        return cipher;
    }

    static String encrypt(String value, PrivateKey key) throws GeneralSecurityException {
        return new String(encodeBase64(encrypt(value.getBytes(StandardCharsets.UTF_8), key)));
    }

    static String decrypt(String encryptedValue, PublicKey key) throws GeneralSecurityException {
        return new String(decrypt(decodeBase64(encryptedValue.getBytes(StandardCharsets.UTF_8)), key));
    }

    static byte[] encrypt(byte[] value, PrivateKey key) throws GeneralSecurityException {
        return createCipher(key, Cipher.ENCRYPT_MODE).doFinal(value);

    }

    static byte[] decrypt(byte[] value, PublicKey key) throws GeneralSecurityException {
        return createCipher(key, Cipher.DECRYPT_MODE).doFinal(value);
    }

    static byte[] encodeBase64(byte[] data) {
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encode(data);
    }

    static byte[] decodeBase64(byte[] data) {
        try {
            return Base64.getDecoder().decode(data);
        } catch (IllegalArgumentException exception) {
            if (exception.getMessage().contains("Illegal base64 character")) {
                throw new AuthException("Passed value is not valid base64 encoded string",
                        ERROR_INVALID_BASE64_VALUE);
            }
            throw exception;
        }
    }

    static SecretKeySpec getSymmetricKey(String key, String algorithm) throws UnsupportedEncodingException {
        return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    static String encSymmetric(String key, String value)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = symmInit(Cipher.ENCRYPT_MODE, key, "AES");
        return new String(encodeBase64(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
    }

    static String decSymmetric(String key, String value)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = symmInit(Cipher.DECRYPT_MODE, key, "AES");
        return new String(cipher.doFinal(decodeBase64(value.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
    }

    static Cipher symmInit(int mode, String key, String algo)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(mode, getSymmetricKey(key, algo));
        return cipher;
    }

    static KeyPair loadFromPKCS12(String alias, File file, char[] password) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
        KeyStore pkcs12KeyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis = new FileInputStream(file);) {
            pkcs12KeyStore.load(fis, password);
        }

        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(password);
        Entry entry = pkcs12KeyStore.getEntry(alias, param);
        if (!(entry instanceof PrivateKeyEntry)) {
            throw new KeyStoreException("That's not a private key!");
        }
        PrivateKeyEntry privKeyEntry = (PrivateKeyEntry) entry;
        PublicKey publicKey = privKeyEntry.getCertificate().getPublicKey();
        PrivateKey privateKey = privKeyEntry.getPrivateKey();
        return new KeyPair(publicKey, privateKey);
    }
}
