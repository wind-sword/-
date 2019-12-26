package hello;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class initializeGraph {
	public enum nod implements Label{
		oversea_enterprise,
		domestic_enterprise
	}
	public static void initialize() {
		GraphDatabaseService gb = new GraphDatabaseFactory().
				newEmbeddedDatabase(new File("D:\\neo4j-community-3.4.0\\data\\databases\\graph.db"));
		Transaction tx = gb.beginTx();
		try {
			ResourceIterator<Node> de_ = gb.findNodes(nod.domestic_enterprise);
			ResourceIterator<Node> oe_ = gb.findNodes(nod.oversea_enterprise);
			List<Node> de = new ArrayList<>();
			List<Node> oe = new ArrayList<>();
			while (de_.hasNext()) {
				de.add(de_.next());
			}
			while (oe_.hasNext()) {
				oe.add(oe_.next());
			}
			for (int i=0;i<de.size();i++) {
			
			}
			tx.success();
		}
		catch(Exception e) {
			tx.failure();
		}
		finally {
			tx.close();
		}
	}
}
