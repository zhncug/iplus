package com.iplus.common.utils;

import com.iplus.common.exception.EncryptException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * Created by zhangli on 17-11-27.
 * des加密解密工具类
 */
public class DesUtils {
    private final static String DES = "DES";

    /**
     * des加密&base64编码
     * @param data
     * @param key  加密键
     * @return
     */
    public static String encrypt(String data, String key) {
        if (null == data) { return null;}
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secureKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            byte[] bt = cipher.doFinal(data.getBytes());
            String result = new BASE64Encoder().encode(bt);
            return result;
        } catch (Exception e) {
            throw new EncryptException("encrypt error", e);
        }
    }

    /**
     * base64解码&des解密
     * @param data
     * @param key 加密键
     * @return
     */
    public static String decrypt(String data, String key) {
        if (null == data) { return null;}
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(data);
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secureKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
            byte[] bt = cipher.doFinal(buf);
            return new String(bt);
        } catch (Exception e) {
            throw new EncryptException("decrypt error", e);
        }
    }
}