# trust-check
A utility to keep CAs in your trust store up-to-date.

In a world where [CAs are untrustworthy](https://groups.google.com/d/msg/mozilla.dev.security.policy/k9PBmyLCi8I/mKSMaz9eCgAJ), it is best to keep your sources of trust as small and up-to-date as possible. The tool compares your current source of trust with the latest versions of upstream trust like the JDK and browser trust stores. The tool writes a report indicate which certificates are present/absent in upstream trust stores. This helps drive decisions to untrust insecure CAs from your application and also guage risk of new additions. This supports both JKS and CA bundles.

## Build
`mvn package`

## Usage
```
 -config <config>         path to application config file contain upstream
                          store information
 -output <output>         output file for report(CSV)
 -storePass <storePass>   trust store password
 -storePath <storePath>   absolute path of target trust store
 -storeType <storeType>   trust store type - JKS/BUNDLE
```

### Examples
* Compare your local JDK trust store
```
java -jar target/trust-check-1.0.jar -storePath $JAVA_HOME/jre/lib/security/cacerts -storePass changeit -storeType JKS`
```

* Write your output as CSV
```
java -jar target/trust-check-1.0.jar -storePath cacerts -storePass changeit -storeType JKS -output report.csv
```

* Compare a local CA bundle
```
java -jar target/trust-check-1.0.jar -storePath /etc/pki/tls/certs/ca-bundle.crt -storeType BUNDLE
```
