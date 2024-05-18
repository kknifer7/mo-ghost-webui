package io.knifer.moghostwebui.common.util;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * - 加密、解密、摘要等操作工具类 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
public final class CodecUtil {

    public final static String SHA256 = "SHA-256";

    public final static String AES = "AES";

    private static final String AES_PADDING = "AES/ECB/PKCS5Padding";

    private CodecUtil(){
        throw new AssertionError();
    }

    public static byte[] base64(@Nonnull String content) {
        Base64.Decoder decoder;

        if (content.isEmpty()){
            return new byte[0];
        }
        decoder = Base64.getDecoder();

        return decoder.decode(content);
    }

    public static byte[] sha256(@Nonnull String message){
        if (message.isEmpty()){
            return new byte[0];
        }

        return getMessageDigest(SHA256).digest(message.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha256String(@Nonnull String message){
        return bytesToHexString(sha256(message));
    }

    private static String bytesToHexString(byte[] bytes){
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            int val = ((int) b) & 0xff;
            if (val < 16) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(val));
        }

        return builder.toString();
    }

    public static byte[] sha256(@Nonnull File file) throws IOException {
        if (file.length() == 0){
            return new byte[0];
        }

        MessageDigest md = getMessageDigest(SHA256);
        byte[] buf = new byte[1024];
        int length;

        try(InputStream in = new FileInputStream(file)){
            while ((length = in.read(buf)) > 0){
                md.update(buf, 0, length);
            }
        }

        return md.digest();
    }

    public static String sha256String(@Nonnull File file) throws IOException {
        return bytesToHexString(sha256(file));
    }

    private static MessageDigest getMessageDigest(String algorithm){
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new MoException(ErrorCodes.UNKNOWN, "MessageDigest create failed, " + e.getMessage());
        }
    }

    public static String encryptToHexString(String algorithm, String source, String key) {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher;
        byte[] encryptedBytes;

        try {
            cipher = Cipher.getInstance(AES_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encryptedBytes = cipher.doFinal(source.getBytes());
        } catch (
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e
        ) {
            throw new MoException(ErrorCodes.UNKNOWN, "Cipher create failed, " + e.getMessage());
        }

        return bytesToHexString(encryptedBytes);
    }

    public static String decryptToString(String algorithm, String source, String key) {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher;
        byte[] decryptedBytes;

        try {
            cipher = Cipher.getInstance(AES_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            decryptedBytes = cipher.doFinal(DatatypeConverter.parseHexBinary(source));
        } catch (
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e
        ) {
            throw new MoException(ErrorCodes.UNKNOWN, "Cipher create failed, " + e.getMessage());
        }

        return new String(decryptedBytes);
    }
}
