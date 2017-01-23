package com.trustcheck.io;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trustcheck.util.PKIUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author varrunr
 */
public class CABundleLoader implements StoreLoader {

    private String caBundleStr;
    private static final Pattern CERTIFICATE_PATTERN = Pattern.compile("(?s)-----BEGIN CERTIFICATE-----(.*?)-----END CERTIFICATE-----");

    public CABundleLoader(String filePath) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            caBundleStr = new String(encoded, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<X509Certificate> getCertificates() {
        Set<X509Certificate> certificates = Sets.newHashSet();
        List<String> pemCertificates = getCertificatesPem();
        for (String pemCert : pemCertificates) {
            certificates.add(PKIUtil.getCertificate(pemCert));
        }
        return certificates;
    }

    public List<String> getCertificatesPem() {
        Matcher matcher = CERTIFICATE_PATTERN.matcher(caBundleStr);
        List<String> trustedRootsPem = Lists.newArrayList();
        while (matcher.find()) {
            String certWithNewLines = matcher.group(1);
            String base64encodedCert = CharMatcher.breakingWhitespace().removeFrom(certWithNewLines);
            trustedRootsPem.add(base64encodedCert);
        }
        return trustedRootsPem;
    }
}
