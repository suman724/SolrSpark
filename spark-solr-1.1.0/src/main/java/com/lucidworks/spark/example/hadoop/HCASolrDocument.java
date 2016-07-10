package com.lucidworks.spark.example.hadoop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

public class HCASolrDocument implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4211530048578681979L;
	
	SolrInputDocument profileDocument;
	List<SolrInputDocument> children = new ArrayList<SolrInputDocument>();
	
	public SolrInputDocument getProfileDocument() {
		return profileDocument;
	}
	public void setProfileDocument(SolrInputDocument profileDocument) {
		this.profileDocument = profileDocument;
	}
	public List<SolrInputDocument> getChildren() {
		return children;
	}
	public void setChildren(List<SolrInputDocument> children) {
		this.children = children;
	}

	
	public void addToChildren(SolrInputDocument child){
		children.add(child);
	}
	
}
