package com.trustcheck.config;

import java.util.List;

/**
 * @author varrunr
 */
public class TrustCheckConfig {
    private List<TrustStoreConfig> trustStores;

    public List<TrustStoreConfig> getTrustStores() {
        return trustStores;
    }

    public void setTrustStores(List<TrustStoreConfig> trustStores) {
        this.trustStores = trustStores;
    }
}
