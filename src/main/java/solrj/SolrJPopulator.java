package solrj;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

/**
 *
 * @author the-ramones
 */
public class SolrJPopulator {

    public static void main(String[] args) throws IOException, SolrServerException {
        System.out.println("Connecting to Solr instance..");
        HttpSolrServer server = new HttpSolrServer("http://localhost:8983/solr/");
        System.out.println("Solr server created..");
        for (int i = 0; i < 1000; ++i) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", "book-" + i);
            doc.addField("title", "book of mormon" + i);
            doc.addField("description", "The Legend of the Hobbit part " + i);
            System.out.println("Solr document created..");
            server.add(doc);
            System.out.println("Solr document added to the index..");
            if (i % 100 == 0) {
                server.commit();  // periodically flush
            }
        }
        System.out.println("The last commit to Solr..");
        server.commit();
    }
}
