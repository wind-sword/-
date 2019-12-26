package test;

import java.io.File;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Cypheruse {
	public static void main(String[] args) {
		GraphDatabaseService gdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
		try(Transaction tx=gdb.beginTx()){
			Result result=gdb.execute("match (n) where id(n)=0 return n");
			while(result.hasNext())
			{
				Map<String,Object> res=result.next();
				System.out.println();
			}
			tx.success();
		}
		gdb.shutdown();
	}
}
