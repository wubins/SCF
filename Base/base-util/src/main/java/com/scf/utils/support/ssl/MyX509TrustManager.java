package com.scf.utils.support.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * x509 https
 * @author wubin
 * @date 2016年7月28日 下午1:55:44 
 * @version V1.1.0
 */
public class MyX509TrustManager implements X509TrustManager
{
    
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
