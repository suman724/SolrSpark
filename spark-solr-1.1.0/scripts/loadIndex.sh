HADOOP_OPTS="-Djava.security.auth.login.config=/home/x003023a/search/jaas.conf" \
hadoop --config /etc/hadoop/conf.cloudera.yarn \
jar /opt/cloudera/parcels/CDH/lib/solr/contrib/mr/search-mr-*-job.jar org.apache.solr.hadoop.MapReduceIndexerTool \
-D 'mapred.child.java.opts=-Xmx500m'  \
--log4j /opt/cloudera/parcels/CDH/share/doc/search*/examples/solr-nrt/log4j.properties \
--morphline-file /home/x003023a/search/morph.conf \
--output-dir hdfs://xsnl50b723w.pharma.aventis.com:8020/user/s000650a/search_org_profile/output \
--reducers -1 \
--verbose --go-live \
--zk-host xsnl50b723w.pharma.aventis.com:2181/solr \
--collection org_profile hdfs://xsnl50b723w.pharma.aventis.com:8020/business/common/am/data/for_purpose/hive/frozen/hca/search_org_profile


