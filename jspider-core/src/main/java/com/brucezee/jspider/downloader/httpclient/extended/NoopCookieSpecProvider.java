package com.brucezee.jspider.downloader.httpclient.extended;

import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.*;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.protocol.HttpContext;

/**
 * 对cookie不进行验证，增加额外的cookie过期时间格式
 * Created by brucezee on 2017/1/9.
 */
public class NoopCookieSpecProvider implements CookieSpecProvider {
    public static final String NAME = "without_validation";
    public static final String DATE_PATTERN_NETSCAPE1 = "EEE, dd-MMM-yy HH:mm:ss z";
    @Override
    public CookieSpec create(HttpContext context) {
        String[] datePatterns = new String[] {
                DateUtils.PATTERN_RFC1123,
                DATE_PATTERN_NETSCAPE1,//default Netscape date pattern
        };
        return new DefaultCookieSpec(datePatterns, false) {
            @Override
            public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
                //do nothing
            }
        };
    }
}