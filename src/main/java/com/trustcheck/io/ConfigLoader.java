package com.trustcheck.io;

import org.yaml.snakeyaml.Yaml;
import com.trustcheck.config.TrustCheckConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author varrunr
 */
public class ConfigLoader {
    public TrustCheckConfig load(String path) {
        try {
            Yaml yaml = new Yaml();
            InputStream in = Files.newInputStream(Paths.get(path));
            TrustCheckConfig configuration = yaml.loadAs(in, TrustCheckConfig.class);
            return configuration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
