package com.sanofi.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

public class SolrClient {
	
	public static void main(String args[]){
		
		//System.setProperty("java.security.auth.login.config", "C:\\Projects\\workspace\\solrj-example\\jaas.conf");

		
		SolrServer server = new HttpSolrServer("http://xsnl50b722p.pharma.aventis.com:8983/solr/hca");
		//CloudSolrServer server1 = new CloudSolrServer("xsnl50b724b.pharma.aventis.com:2181,xsnl50b726t.pharma.aventis.com:2181,xsnl50b723w.pharma.aventis.com:2181/solr");
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		QueryResponse response = null;
		
		try{
			response = server.query(query);
			SolrDocumentList docs = response.getResults();
			System.out.println("Number of documents" +docs.getNumFound());
		}catch(SolrException | SolrServerException ex){
			ex.printStackTrace();
		}
	}

}
