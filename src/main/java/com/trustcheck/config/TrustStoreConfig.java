package com.trustcheck.config;

/**
 * @author varrunr
 */
public class TrustStoreConfig {
    private String alias;
    private String path;
    private StoreType type;
    private String password;

    private TrustStoreConfig() {

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StoreType getType() {
        return type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
