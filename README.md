## Description
Directory JSON tree generator.

## Usage 
[maven usage](http://maven.apache.org/plugins/maven-assembly-plugin/usage.html)

```shell
mvn package
# jar with src
mvn source:jar
# jar with dependency
mvn assembly:assembly
# sonarqube analysis
compile sonar:sonar deploy -DskipTests -Dsonar.host.url=http://192.1.15.15:9000  -Dsonar.java.binaries=target
```
## Change Log

##### 1.0.6
+ fix path to URL encode
+ add usage -jar <target> <base dir> [new] 
