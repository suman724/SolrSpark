package com.lucidworks.spark.example.hadoop;

import com.lucidworks.spark.SolrSupport;
import com.lucidworks.spark.SparkApp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class NestedSolrRDDProcessor implements SparkApp.RDDProcessor {
  
  public static Logger log = Logger.getLogger(NestedSolrRDDProcessor.class);

  public String getName() { return "nested-collection"; }

  public Option[] getOptions() {
    return new Option[]{
      OptionBuilder
        .withArgName("PATH")
        .hasArg()
        .isRequired(false)
        .withDescription("HDFS path identifying the directories / files to index")
        .create("hdfsPath"),
      OptionBuilder
        .withArgName("INT")
        .hasArg()
        .isRequired(false)
        .withDescription("Queue size for ConcurrentUpdateSolrClient; default is 1000")
        .create("queueSize"),
      OptionBuilder
        .withArgName("INT")
        .hasArg()
        .isRequired(false)
        .withDescription("Number of runner threads per ConcurrentUpdateSolrClient instance; default is 2")
        .create("numRunners"),
      OptionBuilder
        .withArgName("INT")
        .hasArg()
        .isRequired(false)
        .withDescription("Number of millis to wait until CUSS sees a doc on the queue before it closes the current request and starts another; default is 20 ms")
        .create("pollQueueTime")
    };
  }

  // Benchmarking dataset generated by Solr Scale Toolkit
  /*private static final String[] pigSchema =
    ("cust_id,org_nm,non_std_org_nm,org_alt_nm,non_std_org_alt_nm,org_enty_type,org_type_cd,org_sub_type_cd,org_spec_cd,org_sub_spec_cd,bu_id,is_active,ins_time,end_time,last_mod_time,last_mod_by,dqm_flag,dqm_err_detail,acd_flag").split(",");

  */
  public int run(SparkConf conf, CommandLine cli) throws Exception {
    JavaSparkContext jsc = new JavaSparkContext(conf);

    
    /*List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
    for(int i=0; i<100; i++){
		SolrInputDocument document = new SolrInputDocument();
		document.addField("Id", i);
		document.addField("Type", "Product");
		document.addField("StockNumber", "100-"+i);

		SolrInputDocument childDoc = new SolrInputDocument();
		childDoc.addField("Id", "1.1."+i);
		childDoc.addField("Type", "Review");
		childDoc.addField("TagName", "Rview of product");
		childDoc.addField("TagUser", "Suman");
		childDoc.addField("TagType", "1");
		

		document.addChildDocument(childDoc);
		
		childDoc = new SolrInputDocument();
		childDoc.addField("Id", "1.2."+i);
		childDoc.addField("Type", "Review");
		childDoc.addField("TagName", "Rview of product");
		childDoc.addField("TagUser", "Swetha");
		childDoc.addField("TagType", "2");
		document.addChildDocument(childDoc);


		log.info("Document "+ i +"\t"+document.toString());
		documents.add(document);
    }
    
    JavaRDD<SolrInputDocument> docsRDD =jsc.parallelize(documents);
    */
    List<Integer> documents = new ArrayList<Integer>();
    for(int i=0; i<100; i++){
    	documents.add(i);
    }
    JavaRDD<Integer> idRDD = jsc.parallelize(documents);
    
    
    JavaRDD<SolrInputDocument> docsRDD = idRDD.map(new Function<Integer, SolrInputDocument>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5416891081797311147L;

		@Override
		public SolrInputDocument call(Integer i) throws Exception {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("Id", i.intValue());
			document.addField("Type", "Product");
			document.addField("StockNumber", "100-"+i);

			SolrInputDocument childDoc = new SolrInputDocument();
			childDoc.addField("Id", "1.1."+i);
			childDoc.addField("Type", "Review");
			childDoc.addField("TagName", "Rview of product");
			childDoc.addField("TagUser", "Suman");
			childDoc.addField("TagType", "1");
			

			document.addChildDocument(childDoc);
			
			childDoc = new SolrInputDocument();
			childDoc.addField("Id", "1.2."+i);
			childDoc.addField("Type", "Review");
			childDoc.addField("TagName", "Rview of product");
			childDoc.addField("TagUser", "Swetha");
			childDoc.addField("TagType", "2");
			document.addChildDocument(childDoc);


			log.info("Document "+ i +"\t"+document.toString());
			
			return document;
		}
    	
	});
    

    String zkHost = cli.getOptionValue("zkHost", "localhost:9983");
    
    String collection = cli.getOptionValue("collection", "nested_collection");
    int queueSize = Integer.parseInt(cli.getOptionValue("queueSize", "1000"));
    int numRunners = Integer.parseInt(cli.getOptionValue("numRunners", "2"));
    int pollQueueTime = Integer.parseInt(cli.getOptionValue("pollQueueTime", "20"));
    
    System.out.println("***************************************************************************");
    log.info("********************************************");
    log.info("zkHost" + zkHost);
    log.info("Collection " + collection);
    log.info("queueSize " + queueSize);
    log.info("numRunners " + numRunners);
    log.info("pollQueueTime " + pollQueueTime);
    log.info("********************************************");
    System.out.println("***************************************************************************");
    
    //SolrSupport.streamDocsIntoSolr(zkHost, collection, "id", pairs, queueSize, numRunners, pollQueueTime);
    log.info("calling indexDocs");
    SolrSupport.indexDocs(zkHost, collection, 100, docsRDD);
    log.info("indexDocs called");
    
    // send a final commit in case soft auto-commits are not enabled
    CloudSolrClient cloudSolrClient = SolrSupport.getSolrServer(zkHost);
    log.info("Got cloudSolrClient ");
    cloudSolrClient.setDefaultCollection(collection);
    log.info("Collection set");
    cloudSolrClient.commit(true, true);
    log.info("Commit");
    cloudSolrClient.close();
    log.info("Closed");

    return 0;
  }
}
