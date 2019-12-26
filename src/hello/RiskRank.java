package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import hello.FindSamePhone.LabelTypes;
import jxl.Sheet;
import jxl.Workbook;

public class RiskRank {	
	List<String> cdeid=new ArrayList<String>();
	List<String> coename=new ArrayList<String>();//境内外黑主体列表
	
	List<Long> cdeID=new ArrayList<Long>();
	List<Long> coeID=new ArrayList<Long>();//境内外黑主体id列表
	
	List<Long> crimeID=new ArrayList<Long>();//合并用
	
	public void rxlce() {
		try {			
			InputStream is = new FileInputStream(new File("   ").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			for (int i=1;i<sheet.getRows();i++) {
				if(sheet.getCell(0, i).getContents()=="境内主体")
					cdeid.add(sheet.getCell(1, i).getContents());
				else
					coename.add(sheet.getCell(1, i).getContents());
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void turntoid() {
		GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
		try(Transaction tx=graphDB.beginTx()){
			for(int i=0;i<cdeid.size();i++) {
				Node node=graphDB.findNode(LabelTypes.domestic_enterprise, "eid", cdeid.get(i));
				if(node!=null)
					cdeID.add(node.getId());
			}
			
			for(int j=0;j<coename.size();j++) {
				Node node=graphDB.findNode(LabelTypes.oversea_enterprise, "engname", coename.get(j));
				if(node!=null)
					coeID.add(node.getId());
			}
			System.out.println("黑名单主体转换为id完成！境内发现"+cdeID.size()+"境外"+coeID.size()+"个");
			
			crimeID=cdeID;//合并
			crimeID.addAll(coeID);
			graphDB.shutdown();
		}
	}
	
}
