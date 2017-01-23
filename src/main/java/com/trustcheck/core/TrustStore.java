package com.trustcheck.core;

import com.google.common.collect.ImmutableSet;
import com.trustcheck.io.StoreLoader;
import com.trustcheck.util.PKIUtil;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Set;

/**
 * @author varrunr
 */
public class TrustStore {
    private Set<String> certificateFingerprints;
    private Set<X509Certificate> trustedRoots;
    private String alias;

    public TrustStore(String alias, StoreLoader loader) {
        this.trustedRoots = loader.getCertificates();
        this.alias = alias;
        ImmutableSet.Builder<String> fingerprintBuilder = new ImmutableSet.Builder<>();
        for (X509Certificate root : trustedRoots) {
            fingerprintBuilder.add(PKIUtil.getFingerprint(root));
        }
        this.certificateFingerprints = fingerprintBuilder.build();
    }

    public Collection<X509Certificate> getTrustedRoots() {
        return trustedRoots;
    }

    public boolean containsCertificate(X509Certificate certificate) {
        return certificateFingerprints.contains(PKIUtil.getFingerprint(certificate));
    }

    public boolean containsCertificate(String certificateFingerprint) {
        return certificateFingerprints.contains(certificateFingerprint);
    }

    public String getAlias() {
        return alias;
    }

    public int size() {
        return certificateFingerprints.size();
    }

    @Override
    public String toString() {
        return alias;
    }
}
