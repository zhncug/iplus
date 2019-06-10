package com.iplus.common.utils;

import com.qt.wisteria.common.api.BusinessException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zhangrui on 17-3-1.
 */
public class FreeMarkerWriter {

    private static final Logger logger = LogUtils.getLogger(FreeMarkerWriter.class);

    private static Configuration cfg;

    private static StringTemplateLoader loader;

    private static final FreeMarkerWriter freeMarkerWriter = new FreeMarkerWriter();

    private FreeMarkerWriter() {
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        loader = new StringTemplateLoader();
        cfg.setTemplateLoader(loader);
    }

    public static FreeMarkerWriter getInstance() {
        return freeMarkerWriter;
    }

    public String toString(String source, Object content) {
        try {
            String md5 = getMD5(source);
            Object obj = loader.findTemplateSource(md5);
            if(obj == null) {
                loader.putTemplate(md5, source);
            }

            Template template = cfg.getTemplate(md5, "utf-8");
            StringWriter writer = new StringWriter();
            template.process(content, writer);
            return writer.toString();
        } catch (Exception e) {
            logger.error("freemarker convert error!", e);
        }

        return "";
    }

    private String getMD5(String source){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new BusinessException("MD5 encode error", e);
        }
    }
}
