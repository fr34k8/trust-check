package com.trustcheck.main;

import com.google.common.collect.Lists;
import com.trustcheck.io.ConfigLoader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.trustcheck.config.TrustCheckConfig;
import com.trustcheck.config.TrustStoreConfig;
import com.trustcheck.diff.TrustStoreComparator;
import com.trustcheck.io.Loader;
import com.trustcheck.core.TrustStore;

import java.util.List;

/**
 * @author varrunr
 */
public class TrustCheckApplication {

    private static final String DEFAULT_CONFIG = "resources/config.yml";

    public static void main(String[] args) throws Exception {
        Option storePath = Option.builder("storePath")
                .argName("storePath")
                .desc("absolute path of target trust store")
                .hasArg()
                .required()
                .type(String.class)
                .build();

        Option storeType = Option.builder("storeType")
                .argName("storeType")
                .desc("trust store type - JKS/BUNDLE")
                .hasArg()
                .required()
                .type(String.class)
                .build();

        // Required only for storeType JKS
        Option storePass = Option.builder("storePass")
                .argName("storePass")
                .desc("trust store password")
                .hasArg()
                .type(String.class)
                .build();

        // Optional
        Option output = Option.builder("output")
                .argName("output")
                .desc("output file for report")
                .hasArg()
                .type(String.class)
                .build();

        // Optional
        Option configFile = Option.builder("config")
                .argName("config")
                .desc("path to application config file contain upstream store information")
                .hasArg()
                .type(String.class)
                .build();

        Options options = new Options();
        options.addOption(storePath);
        options.addOption(storePass);
        options.addOption(storeType);
        options.addOption(output);
        options.addOption(configFile);

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("trust-check", options);
            return;
        }

        ConfigLoader configLoader = new ConfigLoader();
        String configPath = line.hasOption(configFile.getOpt()) ? line.getOptionValue(configFile.getOpt()) : DEFAULT_CONFIG;
        TrustCheckConfig config = configLoader.load(configPath);

        TrustStore mainStore = new TrustStore("main",
                                            Loader.getLoader(line.getOptionValue(storePath.getOpt()),
                                                             line.getOptionValue(storePass.getOpt()),
                                                             line.getOptionValue(storeType.getOpt())));

        List<TrustStore> storesToCompare = Lists.newArrayList();
        List<TrustStoreConfig> storeConfigs = config.getTrustStores();
        for (TrustStoreConfig storeConfig : storeConfigs) {
            TrustStore upstreamStore = new TrustStore(storeConfig.getAlias(), Loader.getLoader(storeConfig));
            storesToCompare.add(upstreamStore);
        }

        TrustStoreComparator comparator = new TrustStoreComparator(mainStore, storesToCompare);
        comparator.compare();
        if (line.hasOption(output.getOpt())) {
            comparator.writeReportToFile(line.getOptionValue(output.getOpt()));
        }
        System.out.println(comparator.getFormattedReport());
    }
}
