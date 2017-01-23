package com.trustcheck.diff;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.trustcheck.core.TrustStore;
import com.trustcheck.util.PKIUtil;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.trustcheck.util.PKIUtil.formatFingerprint;
import static com.trustcheck.util.PKIUtil.getFingerprint;

/**
 * @author varrunr
 */
public class TrustStoreComparator {
    private TrustStore mainStore;
    private List<TrustStore> toCompare;
    private boolean[][] certificatePresenceMatrix;

    public TrustStoreComparator(TrustStore mainStore, List<TrustStore> trustStores) {
        this.mainStore = mainStore;
        this.toCompare = trustStores;
    }

    public void compare() {
        certificatePresenceMatrix = new boolean[mainStore.size()][toCompare.size()];
        List<X509Certificate> rootsToCompare = Lists.newArrayList(mainStore.getTrustedRoots());
        for (int certCt = 0; certCt < rootsToCompare.size(); certCt++) {
            X509Certificate certificate = rootsToCompare.get(certCt);
            for (int storeCt = 0; storeCt < toCompare.size(); storeCt++) {
                certificatePresenceMatrix[certCt][storeCt] = toCompare.get(storeCt).containsCertificate(certificate);
            }
        }
    }

    public void writeReportToFile(String path) {
        try {
            Files.write(getCSV(), new File(path), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCSV() {
        StringBuilder sb = new StringBuilder();

        String header = Joiner.on(",").join(mainStore.getAlias(), toCompare);
        sb.append(header);
        sb.append("\n");

        List<X509Certificate> rootsToCompare = Lists.newArrayList(mainStore.getTrustedRoots());
        for (int certCt = 0; certCt < rootsToCompare.size(); certCt++) {
            X509Certificate certificate = rootsToCompare.get(certCt);
            String dn = certificate.getSubjectDN().getName().replace("\"", "");
            String fingerprint = PKIUtil.formatFingerprint(PKIUtil.getFingerprint(certificate));
            sb.append(String.format("\"%s\",%s", dn, fingerprint));
            for (int storeCt = 0; storeCt < toCompare.size(); storeCt++) {
                sb.append(String.format(",%b", certificatePresenceMatrix[certCt][storeCt]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-100s", mainStore.getAlias()));
        for (TrustStore store : toCompare) {
            sb.append(String.format("%-30s", store.getAlias()));
        }
        sb.append("\n\n");
        List<X509Certificate> rootsToCompare = Lists.newArrayList(mainStore.getTrustedRoots());
        for (int certCt = 0; certCt < rootsToCompare.size(); certCt++) {
            X509Certificate certificate = rootsToCompare.get(certCt);
            String dn = certificate.getSubjectDN().getName();
            List<String> parts = Lists.newArrayList(Splitter.on(",").split(dn.substring(0, Math.min(100, dn.length())).trim()));

            sb.append(String.format("%-100s", parts.get(0)));
            for (int storeCt = 0; storeCt < toCompare.size(); storeCt++) {
                sb.append(String.format("%-30b", certificatePresenceMatrix[certCt][storeCt]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
