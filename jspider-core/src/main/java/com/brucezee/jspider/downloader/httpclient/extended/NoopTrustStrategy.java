package com.brucezee.jspider.downloader.httpclient.extended;

import org.apache.http.conn.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 默认证书信任策略，信任所有证书
 * Created by brucezee on 2017/1/9.
 */
public class NoopTrustStrategy implements TrustStrategy {
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
    }
}
