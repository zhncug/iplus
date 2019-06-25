package com.iplus.wechat.common.utils;

import com.iplus.wechat.common.exception.DecryptException;
import com.iplus.wechat.common.exception.EncryptException;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by zhangrui on 17-5-6.
 */
public class EncryptUtils {

    private transient static final String HIDE_PWD = "******";

    /**
     * SHA1 安全加密算法
     * @return
     */
    public static String SHA1(String decrypt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(decrypt.getBytes());
        //获取字节数组
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    public static String hidePwd(String pwd, int length) {
        String result = pwd;
        if (StringUtils.isNotBlank(pwd) && pwd.length() > length) {
            result = pwd.substring(0, length-1) + HIDE_PWD;
        }
        return result;
    }

    public static String md5(String str) throws NoSuchAlgorithmException {
        String result = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        result = buf.toString();
        return result;
    }

    public static String md5WhithBase64(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    public static byte[] encryptByAES(byte[] content, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new EncryptException("encrypt error", e);
        }
    }

    public static byte[] decryptByAES(byte[] content, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new DecryptException("encrypt error", e);
        }
    }
}
