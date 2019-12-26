package hello;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import util.*;
import java.util.*;

import algorithm.Louvain;
public class HelloWorld {	
	 static void writeOutputCircle(String fileName, Louvain a,RiskRank r) throws IOException {
		 r.rxlce();
		 r.turntoid();
		 
         BufferedWriter bufferedWriter;

         ArrayList list[] = new ArrayList[a.global_n];
         for(int i=0;i<a.global_n;i++){
                list[i]=new ArrayList<Integer>();
         }
         for(int i=0;i<a.global_n;i++){
                list[a.global_cluster[i]].add(i);
         }
         
         double[] tightness=a.gettightness();
         
         int p=0;
         for(int i=0;i<a.global_n;i++){
                if(list[i].size()==0)
                       continue;
                
                int cecount=0;//社区内黑结点个数
                int ecount=list[i].size();//社区内总结点个数
                
                bufferedWriter = new BufferedWriter(new FileWriter(fileName+"\\"+String.valueOf(p)+".txt"));
                for(int j=0;j<list[i].size();j++) {
                	if(r.crimeID.contains(a.index1.get(list[i].get(j)))) {
                		r.crimeID.remove(a.index1.get(list[i].get(j)));
                		cecount++;
                	}
                	bufferedWriter.write(a.index1.get(list[i].get(j)).toString()+",");
                }
                p++;
             
                bufferedWriter.close();
         }

  }  
	public static void main(String[] args) throws  IOException {
//		List<bank> banks = readxls.readXlsBank();
//		List<domestic_enterprise> e = readxls.readXlsDomestic();
//		List<oversea_enterprise> o = readxls.readXlsOversea();
//		List<fdi> fd = readxls.readXlsFdi();
//		List<odi> od = readxls.readXlsOdi();
//		List<Management> manage = readxls.readXlsManagement();
//		List<collection> c = readxls.readXlsCollection();
//		List<payment> p  = readxls.readXlsPayment();
//		importdata.importm(manage, e, banks, o, p, c, fd, od);
//		System.out.print("!");
		
//		initializeGraph.initialize();
		RiskRank r=new RiskRank();
		 Louvain a = new Louvain();
          double beginTime = System.currentTimeMillis();
          a.init();
          a.louvain();
          double endTime = System.currentTimeMillis();
          writeOutputCircle("d:\\res",a,r);     //打印每个簇有哪些节点
          System.out.format("program running time: %f seconds%n", (endTime - beginTime) / 1000.0);
         
	
//		FindSamePhone f=new FindSamePhone();
//		f.setUp();
//		f.search();
		
//		SameBank s=new SameBank();
//		s.creatSameBank(s.readXlsPaymentinexp());
	}
}
