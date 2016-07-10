spark-submit --master yarn-server \
  --files "jaas.conf,s000650a.keytab" \
  --class com.lucidworks.spark.SparkApp \
  /home/x003023a/search/spark-solr-1.1.0-shaded.jar \
  nested-collection -zkHost xsnl50b724b.pharma.aventis.com:2181,xsnl50b726t.pharma.aventis.com:2181,xsnl50b723w.pharma.aventis.com:2181/solr  -collection nested_collection \
  -solrJaasAuthConfig=jaas.conf

  #hdfs-to-solr -zkHost xsnl50b722p.pharma.aventis.com:8983  -collection org_profile \
