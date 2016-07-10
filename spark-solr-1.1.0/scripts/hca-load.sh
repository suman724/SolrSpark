spark-submit --master yarn-server --num-executors 5 \
  --files "jaas.conf,s000650a.keytab" \
  --class com.lucidworks.spark.SparkApp \
  /home/x003023a/search/spark-solr-1.1.0-shaded.jar \
  hca-ingestor -zkHost xsnl50b724b.pharma.aventis.com:2181,xsnl50b726t.pharma.aventis.com:2181,xsnl50b723w.pharma.aventis.com:2181/solr  -collection hca \
  -profilePath=hdfs://nameservicedev/business/common/am/data/for_purpose/hive/frozen/hca/search_org_profile \
  -addrPath=hdfs://nameservicedev/business/common/am/data/for_purpose/hive/frozen/hca/search_org_addr \
  -identifierPath=hdfs://nameservicedev/business/common/am/data/for_purpose/hive/frozen/hca/search_org_identifier \
  -solrJaasAuthConfig=jaas.conf

  #hdfs-to-solr -zkHost xsnl50b722p.pharma.aventis.com:8983  -collection org_profile \
