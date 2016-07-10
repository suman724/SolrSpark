package com.sanofi.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;


public class SolrNestedIngestor {
	public static void main(String args[]) {
		SolrServer server = new HttpSolrServer("http://xsnl50b722p.pharma.aventis.com:8983/solr/nested_collection");
		
		
		
		List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
	    for(int i=0; i<10; i++){
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


			documents.add(document);
	    }
	    
        List<SolrInputDocument> batch = new ArrayList<SolrInputDocument>();
	    
	    Iterator<SolrInputDocument> solrInputDocumentIterator = documents.iterator();
	    Date indexedAt = new Date();
        while (solrInputDocumentIterator.hasNext()) {
            SolrInputDocument inputDoc = solrInputDocumentIterator.next();
            inputDoc.setField("_indexed_at_tdt", indexedAt);
            batch.add(inputDoc);
        }

        /*SolrInputDocument document = new SolrInputDocument();
		document.addField("Id", "2");
		document.addField("Type", "Product");
		document.addField("StockNumber", "100-1");

		SolrInputDocument childDoc = new SolrInputDocument();
		childDoc.addField("Id", "2.1");
		childDoc.addField("Type", "Review");
		childDoc.addField("TagName", "Rview of product");
		childDoc.addField("TagUser", "Suman");
		childDoc.addField("TagType", "1");
		

		document.addChildDocument(childDoc);
		
		childDoc = new SolrInputDocument();
		childDoc.addField("Id", "2.2");
		childDoc.addField("Type", "Review");
		childDoc.addField("TagName", "Rview of product");
		childDoc.addField("TagUser", "Swetha");
		childDoc.addField("TagType", "2");
		document.addChildDocument(childDoc);
		
		System.out.println("Document " + document);
		*/
		try {
			long startTime = System.currentTimeMillis();
			UpdateRequest req = new UpdateRequest();
			req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
			
			req.add(batch);
			
			
			UpdateResponse rsp = req.process(server);
			System.out.print("Added documents to solr. Time taken = " + rsp.getElapsedTime() + ". " + rsp.toString() );
			long endTime = System.currentTimeMillis();
			System.out.println(" , time-taken=" + ((double) (endTime - startTime)) / 1000.00 + " seconds");
			
			System.out.println("Named list size - " +rsp.getResponse().size());

		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		} 
	}
}
