package com.trustcheck.io;

import com.trustcheck.config.StoreType;
import com.trustcheck.config.TrustStoreConfig;

/**
 * @author varrunr
 */
public class Loader {
    public static StoreLoader getLoader(TrustStoreConfig config) {
        return getLoader(config.getPath(), config.getPassword(), config.getType());
    }

    public static StoreLoader getLoader(String filePath, String password, String type) {
        if ("JKS".equals(type)) {
            return new JKSLoader(filePath, password.toCharArray());
        } else if("BUNDLE".equals(type)) {
            return new CABundleLoader(filePath);
        } else {
            throw new IllegalStateException("Unsupported trust store type: " + type +
                    "Supported core: JKS/BUNDLE");
        }
    }

    private static StoreLoader getLoader(String filePath, String password, StoreType type) {
        switch (type) {
            case JKS:
                return new JKSLoader(filePath, password.toCharArray());
            case BUNDLE:
                return new CABundleLoader(filePath);
            default:
                throw new IllegalStateException("Unsupported trust store type: " + type +
                        "Supported core: JKS/BUNDLE");
        }
    }
}
