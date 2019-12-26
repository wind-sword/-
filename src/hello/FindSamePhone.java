package hello;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class FindSamePhone {
	public static GraphDatabaseService db;
	
	public static enum LabelTypes implements Label
	{
		domestic_enterprise,oversea_enterprise
	}
	
	public static enum RelTypes implements RelationshipType
	{
		samep
	}
	
	public void setUp() throws IOException
	{
		// FileUtils.deleteRecursively( DB_PATH );
		db = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"))
//				.loadPropertiesFromFile(Neo4jConfPath+"neo4j.conf")
				.newGraphDatabase();
		registerShutdownHook();
	}
	
	private void registerShutdownHook()
	{

		Runtime.getRuntime()

				.addShutdownHook(new Thread()

		{

					@Override

					public void run()

			{

						db.shutdown();

					}

				});

	}
	
	public void search(){
		int count=0;
		try(Transaction tx=db.beginTx()){
			List<Node> nodelist=new ArrayList<Node>();
			Iterator<Node> nodes=db.findNodes(LabelTypes.domestic_enterprise);
			while(nodes.hasNext())
			{
				nodelist.add(nodes.next());
			}
			for(int i=0;i<nodelist.size();i++)
			{
				if(nodelist.get(i).hasProperty("relatephone")){
					for(int j=i+1;j<nodelist.size();j++) {
						if((nodelist.get(j).hasProperty("relatephone")))
						{				
							if((String) nodelist.get(i).getProperty("relatephone")==(String) nodelist.get(j).getProperty("relatephone"))
							{	
								Relationship relsamep=nodelist.get(i).createRelationshipTo(nodelist.get(j), RelTypes.samep);
								count++;
							}
						}
					}
				}
			}
			tx.success();
		}		
		System.out.println("存在"+count+"条同号码关系");
	}
	
}
