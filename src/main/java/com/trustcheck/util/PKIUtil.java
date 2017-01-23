package com.trustcheck.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author varrunr
 */
public class PKIUtil {

    private static final String HASHING_ALGO = "SHA-1";

    public static String getFingerprint(X509Certificate certificate) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASHING_ALGO);
            byte[] der = certificate.getEncoded();
            md.update(der);
            byte[] digest = md.digest();
            return Hex.encodeHexString(digest);
        } catch (NoSuchAlgorithmException | CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatFingerprint(String hexString) {
        List<String> splitString = Splitter.fixedLength(2).splitToList(hexString.toUpperCase());
        return Joiner.on(":").join(splitString);
    }

    public static X509Certificate getCertificate(String pemEncodedCertificate) {
        try {
            byte[] certificateDecoded = Base64.decodeBase64(pemEncodedCertificate);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = new ByteArrayInputStream(certificateDecoded);
            return (X509Certificate) cf.generateCertificate(inputStream);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }
}
