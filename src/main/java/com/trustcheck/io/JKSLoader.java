package com.trustcheck.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author varrunr
 */
public class JKSLoader implements StoreLoader {
    private KeyStore keyStore;

    public JKSLoader(String filePath, char[] password) {
        try {
            loadKeyStore(filePath, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<X509Certificate> getCertificates() {
        try {
            List<String> aliases = Collections.list(keyStore.aliases());
            Set<X509Certificate> certificates = new HashSet<>();
            for (String alias : aliases) {
                Certificate certificate = keyStore.getCertificate(alias);
                if ("X.509".equals(certificate.getType())) {
                    certificates.add((X509Certificate) certificate);
                }
            }
            return certificates;
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadKeyStore(String filePath, char[] password) throws IOException {
        try {
            keyStore = KeyStore.getInstance("JKS");
            FileInputStream inputStream = new FileInputStream(filePath);
            keyStore.load(inputStream, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new IOException(e);
        }
    }
}
