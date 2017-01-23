package com.trustcheck.io;

import java.security.cert.X509Certificate;
import java.util.Set;

/**
 * @author varrunr
 */
public interface StoreLoader {
    Set<X509Certificate> getCertificates();
}
