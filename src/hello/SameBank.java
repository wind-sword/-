package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import hello.FindSamePhone.LabelTypes;
import jxl.Sheet;
import jxl.Workbook;
import util.payment4bank;

public class SameBank {	
	public static List<payment4bank> readXlsPayment4bank(){
		File file  = new File("d:\\payment");
		File[] lists = file.listFiles();
		List<payment4bank> list = new ArrayList<>();
		for (File f : lists) {
			try {
				InputStream is = new FileInputStream(f.getAbsolutePath());
				Workbook wb = Workbook.getWorkbook(is);
				Sheet sheet = wb.getSheet(0);
				
				for (int i=2;i<sheet.getRows();i++) {
					payment4bank m = new payment4bank(sheet.getCell(5, i).getContents(),
							sheet.getCell(15, i).getContents(),
							sheet.getCell(16, i).getContents());
					list.add(m);
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		return list;
	}
	
	public static List<payment4bank> readXlsPaymentinexp(){				
		try {
			InputStream is = new FileInputStream(new File("C:\\Users\\zcs\\Desktop\\testdat\\test.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<payment4bank> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				payment4bank m = new payment4bank(sheet.getCell(5, i).getContents(),
						sheet.getCell(15, i).getContents(),
						sheet.getCell(16, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
	public static enum RelTypes implements RelationshipType
	{
		samebank
	}
	
	public Map<String,Long> getnodesid(){//提供一个企业id与企业结点id转换的方法
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
		Map<String,Long> idtransfer=new HashMap<>();
		try(Transaction tx=graphDb.beginTx()){
			Iterator<Node> nodes=graphDb.findNodes(LabelTypes.domestic_enterprise);
			while(nodes.hasNext()) {
				Node node=nodes.next();
				idtransfer.put((String) node.getProperty("eid"), node.getId());
			}
			graphDb.shutdown();
		}
		return idtransfer;
				
	}
	
	public void creatSameBank(List<payment4bank> list) {
		Map<String,Long> idtra=getnodesid();
		
		BatchInserter bi = null;
		try {						
			bi= BatchInserters.inserter(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
			
			Map<String,Set<String>> set4pay=new HashMap<>();
			for(int i=0;i<list.size();i++) {//每个payid对应一个set
				if(!set4pay.containsKey(list.get(i).getPayid())) {
					Set<String> eset=new HashSet<>();
					set4pay.put(list.get(i).getPayid(), eset);
				}
			}
			
			for(int j=0;j<list.size();j++) {//对应的set加入银行id
				Set<String> setuse=set4pay.get(list.get(j).getPayid());
				setuse.add(list.get(j).getBankid());
			}
			
			List<String> keylist=new ArrayList<String>();//为map的key值即payid固定顺序以便后续比较
			for(String s:set4pay.keySet()) {
				keylist.add(s);
			}
			
			int count=0;
			for(int n=0;n<keylist.size();n++) {
				for(int m=n+1;m<keylist.size();m++) {
					Set<String> setn=set4pay.get(keylist.get(n));
					Set<String> setm=set4pay.get(keylist.get(m));
					Set<String> setsame=new HashSet<>();
					setsame.addAll(setn);
					setsame.retainAll(setm);
					Map<String,String> pro=new HashMap<>();
					pro.put("weight", Integer.toString(setsame.size()));
					if(setsame.size()>0) {
						bi.createRelationship(idtra.get(keylist.get(n)), idtra.get(keylist.get(m)),RelTypes.samebank, null);
						count++;
					}
				}
			}
			System.out.println("创建了"+count+"条同银行关系");
		}catch(Exception e) {
			System.out.println(e);
		}finally
		{
		    if ( bi != null )
		    {
		        bi.shutdown();
		    }
		}
	}
}
