package hello;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import util.*;

public class importdata {
	public static long s=0;
	public static Map<String,Long> ind = new HashMap<>();
	public static Map<String,Long> deind = new HashMap<>();
	public static Map<String,Long> deind_ = new HashMap<>();
	public static Map<String,Long> ovind = new HashMap<>();
	public static Map<String,Long> ovind_ = new HashMap<>();
	public static Map<String,Integer> invests = new HashMap<>();
	public static Map<String,Integer> in = new HashMap<>();
	public static void importm(List<Management> lists,List<domestic_enterprise> de, 
								List<bank> ba , List<oversea_enterprise> ov , List<payment> pay,
								List<collection> co , List<fdi> fd, List<odi> od) {
		BatchInserter bi = null;
		try {
			bi= BatchInserters.inserter(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
			Label l = DynamicLabel.label("Management");
		//	bi.createDeferredSchemaIndex(l).on("name").create();
			Label d = DynamicLabel.label("domestic_enterprise");
		//	bi.createDeferredSchemaIndex(d).on("name").create();
			Label b = DynamicLabel.label("bank");
		//	bi.createDeferredSchemaIndex(b).on("name").create();
			Label o = DynamicLabel.label("oversea_enterprise");
		//	bi.createDeferredSchemaIndex(o).on("chnname").create();
			
			
			for (int i=0;i<lists.size();i++) {//创建外管局结点
				bi.createNode(s, lists.get(i).map(), l);
				ind.put(lists.get(i).getId(), s);//用ind将外管局代码与结点id对应
				s++;
			}
			RelationshipType contain = DynamicRelationshipType.withName("contain");
			RelationshipType manage = DynamicRelationshipType.withName("manage");
			RelationshipType payment = DynamicRelationshipType.withName("payment");
			RelationshipType invest = DynamicRelationshipType.withName("invest");
			RelationshipType paysum = DynamicRelationshipType.withName("paysum");
			RelationshipType investsum = DynamicRelationshipType.withName("investsum");
			for (int i=0;i<lists.size();i++) {//外管局结点创建完从头检查创建上下属关系
				if (ind.containsKey(lists.get(i).getSuperid())) {
					bi.createRelationship(ind.get(lists.get(i).getSuperid()), ind.get(lists.get(i).getId()), contain, null);
				}
				else {//没有的superid则创建新节点
					Map<String,Object> ex = new HashMap<>();
					ex.put("mid", lists.get(i).getSuperid());
					bi.createNode(s, ex, l);
					ind.put(lists.get(i).getSuperid(), s);
					s++;
					bi.createRelationship(ind.get(lists.get(i).getSuperid()), ind.get(lists.get(i).getId()), contain, null);
				}
			}
			for (int i=0;i<de.size();i++) {
				bi.createNode(s, de.get(i).map(), d);
				deind.put(de.get(i).getId(), s);
				deind_.put(de.get(i).getName(), s);
				if (ind.containsKey(de.get(i).getMid())) {
					bi.createRelationship(ind.get(de.get(i).getMid()), s, manage, null);
				}
				s++;
			}
			for (int i=0;i<ov.size();i++) {
				bi.createNode(s, ov.get(i).map(), o);
				ovind.put(ov.get(i).getId(), s);
				ovind_.put(ov.get(i).getChnname(), s);
				s++;
			}
			for (int i=0;i<fd.size();i++) {
				if (ovind.containsKey(fd.get(i).getFid())) {
					//bi.createRelationship(ovind.get(fd.get(i).getFid()), deind.get(fd.get(i).getDid()), invest, null);
					String p = String.valueOf(ovind.get(fd.get(i).getFid()));
					String q = String.valueOf(deind.get(fd.get(i).getDid()));
					if (!in.containsKey(p+" "+q)) {
					//	bi.createRelationship(ovind.get(fd.get(i).getFid()), deind.get(fd.get(i).getDid()), investsum, null);
						in.put(p+" "+q, 1);
						in.put(q+" "+p, 1);
					}
					else {
						int pop = in.get(p+" "+q);
						in.put(p+" "+q, pop+1);
						in.put(q+" "+p, pop+1);
					}
				}
				else {
					Map<String,Object> mp = new HashMap<>();
					mp.put("oid", fd.get(i).getFid());
					bi.createNode(s, mp, o);
					ovind.put(fd.get(i).getFid(), s);
					s++;
					//bi.createRelationship(ovind.get(fd.get(i).getFid()), deind.get(fd.get(i).getDid()), invest, null);
					String p = String.valueOf(ovind.get(fd.get(i).getFid()));
					String q = String.valueOf(deind.get(fd.get(i).getDid()));
					if (!in.containsKey(p+" "+q)) {
					//	bi.createRelationship(ovind.get(fd.get(i).getFid()), deind.get(fd.get(i).getDid()), investsum, null);
						in.put(p+" "+q, 1);
						in.put(q+" "+p, 1);
					}
					else {
						int pop = in.get(p+" "+q);
						in.put(p+" "+q, pop+1);
						in.put(q+" "+p, pop+1);
					}
				}
			}
			for (int i=0;i<od.size();i++) {
				if (ovind.containsKey(od.get(i).getFid())) {
				//bi.createRelationship(deind.get(od.get(i).getDid()),ovind.get(od.get(i).getFid()), invest, null);
				String p = String.valueOf(ovind.get(fd.get(i).getFid()));
				String q = String.valueOf(deind.get(fd.get(i).getDid()));
				if (!in.containsKey(p+" "+q)) {
				//	bi.createRelationship(ovind.get(fd.get(i).getFid()), deind.get(fd.get(i).getDid()), investsum, null);
					in.put(p+" "+q, 1);
					in.put(q+" "+p, 1);
				}
				else {
					int pop = in.get(p+" "+q);
					in.put(p+" "+q, pop+1);
					in.put(q+" "+p, pop+1);
				}
				}
				else {
					Map<String,Object> mp = new HashMap<>();
					mp.put("oid", od.get(i).getFid());
					bi.createNode(s, mp, o);
					ovind.put(od.get(i).getFid(), s);
					s++;
					//bi.createRelationship(deind.get(od.get(i).getDid()),ovind.get(od.get(i).getFid()), invest, null);
					String p = String.valueOf(deind.get(od.get(i).getDid()));
					String q = String.valueOf(ovind.get(od.get(i).getFid()));
					if (!in.containsKey(p+" "+q)) {
					//	bi.createRelationship(deind.get(od.get(i).getDid()), ovind.get(od.get(i).getFid()), investsum, null);
						in.put(p+" "+q, 1);
						in.put(q+" "+p, 1);
					}
					else {
						int pop = in.get(p+" "+q);
						in.put(p+" "+q, pop+1);
						in.put(q+" "+p, pop+1);
					}
				}
			}
			for (int i=0;i<co.size();i++) {
				if (co.get(i).getPayname().startsWith("(JW)")) {
					String str = co.get(i).getPayname().substring(4, co.get(i).getPayname().length());
					if (ovind_.containsKey(str)) {
						if (deind.containsKey(co.get(i).getReid())){
						//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(ovind_.get(str));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map = new HashMap<>();
							map.put("eid", co.get(i).getReid());
							map.put("name", co.get(i).getRename());
							bi.createNode(s, map, d);//添加表中不存在的主体
							deind.put(co.get(i).getReid(), s);
							deind_.put(co.get(i).getRename(), s);
							s++;
						//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(ovind_.get(str));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}else {
						if (deind.containsKey(co.get(i).getReid())){
							Map<String,Object> map = new HashMap<>();
							map.put("chnname", str);
							bi.createNode(s, map, o);
							ovind_.put(str, s);
							s++;
						//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(ovind_.get(str));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map_ = new HashMap<>();
							map_.put("chnname", str);
							bi.createNode(s, map_, o);
							ovind_.put(str, s);
							s++;
							Map<String,Object> map = new HashMap<>();
							map.put("eid", co.get(i).getReid());
							map.put("name", co.get(i).getRename());
							bi.createNode(s, map, d);
							deind.put(co.get(i).getReid(), s);
							deind_.put(co.get(i).getRename(), s);
							s++;
					//		bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(ovind_.get(str));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(ovind_.get(str), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}
				}
				else {
					if (deind_.containsKey(co.get(i).getPayname())) {
						if (deind.containsKey(co.get(i).getReid())){
					//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(deind_.get(co.get(i).getPayname()));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map = new HashMap<>();
							map.put("eid", co.get(i).getReid());
							map.put("name", co.get(i).getRename());
							bi.createNode(s, map, d);
							deind.put(co.get(i).getReid(), s);
							deind_.put(co.get(i).getRename(), s);
							s++;
					//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(deind_.get(co.get(i).getPayname()));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}else {
						if (deind.containsKey(co.get(i).getReid())){
							Map<String,Object> map = new HashMap<>();
							map.put("name", co.get(i).getPayname());
							bi.createNode(s, map, d);
							deind_.put(co.get(i).getPayname(), s);
							s++;
					//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(deind_.get(co.get(i).getPayname()));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map_ = new HashMap<>();
							map_.put("name", co.get(i).getPayname());
							bi.createNode(s, map_, d);
							deind_.put(co.get(i).getPayname(), s);
							s++;
							Map<String,Object> map = new HashMap<>();
							map.put("eid", co.get(i).getReid());
							map.put("name", co.get(i).getRename());
							bi.createNode(s, map, d);
							deind.put(co.get(i).getReid(), s);
							deind_.put(co.get(i).getRename(), s);
							s++;
					//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), payment, null);
							String p = String.valueOf(deind_.get(co.get(i).getPayname()));
							String q = String.valueOf(deind.get(co.get(i).getReid()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind_.get(co.get(i).getPayname()), deind.get(co.get(i).getReid()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}
				}
			}
			for (int i=0;i<pay.size();i++) {
				if (pay.get(i).getRename().startsWith("(JW)")) {
					String str = pay.get(i).getRename().substring(4,pay.get(i).getRename().length());
					if (ovind_.containsKey(str)) {
						if (deind.containsKey(pay.get(i).getPayid())) {
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), ovind_.get(str), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(ovind_.get(str));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(deind.get(pay.get(i).getPayid()),ovind_.get(str), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map = new HashMap<>();
							map.put("eid", pay.get(i).getPayid());
							map.put("name", pay.get(i).getPayname());
							bi.createNode(s, map, d);
							deind.put(pay.get(i).getPayid(), s);
							deind_.put(pay.get(i).getPayname(), s);
							s++;
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), ovind_.get(str), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(ovind_.get(str));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(deind.get(pay.get(i).getPayid()),ovind_.get(str), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}else {
						if (deind.containsKey(pay.get(i).getPayid())) {
							Map<String,Object> map = new HashMap<>();
							map.put("chnname", str);
							bi.createNode(s, map, o);
							ovind_.put(str, s);
							s++;
					//		bi.createRelationship(deind.get(pay.get(i).getPayid()), ovind_.get(str), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(ovind_.get(str));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(deind.get(pay.get(i).getPayid()),ovind_.get(str), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map_ = new HashMap<>();
							map_.put("chnname", str);
							bi.createNode(s, map_, o);
							ovind_.put(str, s);
							s++;
							Map<String,Object> map = new HashMap<>();
							map.put("eid", pay.get(i).getPayid());
							map.put("name", pay.get(i).getPayname());
							bi.createNode(s, map, d);
							deind.put(pay.get(i).getPayid(), s);
							deind_.put(pay.get(i).getPayname(), s);
							s++;
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), ovind_.get(str), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(ovind_.get(str));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind.get(pay.get(i).getPayid()),ovind_.get(str), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}
				}
				else {
					if (deind_.containsKey(pay.get(i).getRename())) {
						if (deind.containsKey(pay.get(i).getPayid())) {
					//		bi.createRelationship(deind.get(pay.get(i).getPayid()), deind_.get(pay.get(i).getRename()), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(deind_.get(pay.get(i).getRename()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind.get(pay.get(i).getPayid()),deind_.get(pay.get(i).getRename()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map = new HashMap<>();
							map.put("eid", pay.get(i).getPayid());
							map.put("name", pay.get(i).getPayname());
							bi.createNode(s, map, d);
							deind.put(pay.get(i).getPayid(), s);
							deind_.put(pay.get(i).getPayname(), s);
							s++;
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), deind_.get(pay.get(i).getRename()), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(deind_.get(pay.get(i).getRename()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind.get(pay.get(i).getPayid()),deind_.get(pay.get(i).getRename()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}
					else {
						if (deind.containsKey(pay.get(i).getPayid())) {
							Map<String,Object> map = new HashMap<>();
							map.put("name", pay.get(i).getRename());
							bi.createNode(s, map, d);
							deind_.put(pay.get(i).getRename(), s);
							s++;
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), deind_.get(pay.get(i).getRename()), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(deind_.get(pay.get(i).getRename()));
							if (!invests.containsKey(p+" "+q)) {
						//		bi.createRelationship(deind.get(pay.get(i).getPayid()),deind_.get(pay.get(i).getRename()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
						else {
							Map<String,Object> map_ = new HashMap<>();
							map_.put("name", pay.get(i).getRename());
							bi.createNode(s, map_, d);
							deind_.put(pay.get(i).getRename(), s);
							s++;
							Map<String,Object> map = new HashMap<>();
							map.put("eid", pay.get(i).getPayid());
							map.put("name", pay.get(i).getPayname());
							bi.createNode(s, map, d);
							deind.put(pay.get(i).getPayid(), s);
							deind_.put(pay.get(i).getPayname(), s);
							s++;
				//			bi.createRelationship(deind.get(pay.get(i).getPayid()), deind_.get(pay.get(i).getRename()), payment, null);
							String p = String.valueOf(deind.get(pay.get(i).getPayid()));
							String q = String.valueOf(deind_.get(pay.get(i).getRename()));
							if (!invests.containsKey(p+" "+q)) {
							//	bi.createRelationship(deind.get(pay.get(i).getPayid()),deind_.get(pay.get(i).getRename()), paysum, null);
								invests.put(p+" "+q, 1);
								invests.put(q+" "+p, 1);
							}
							else {
								int pop = invests.get(p+" "+q);
								invests.put(p+" "+q, pop+1);
								invests.put(q+" "+p, pop+1);
							}
						}
					}
				}
			}
			for (Entry<String,Integer> entry : in.entrySet()) {
				String[] str = entry.getKey().split(" ");
				Long start = Long.parseLong(str[0]);
				Long end = Long.parseLong(str[1]);
				Map<String,Object> map = new HashMap<>();
				map.put("weight", entry.getValue());
				if (start<end)
				bi.createRelationship(start, end, investsum, map);
			}
			for (Entry<String,Integer> entry : invests.entrySet()) {
				String[] str = entry.getKey().split(" ");
				Long start = Long.parseLong(str[0]);
				Long end = Long.parseLong(str[1]);
				Map<String,Object> map = new HashMap<>();
				map.put("weight", entry.getValue());
				if (start<end)
				bi.createRelationship(start, end, paysum, map);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			if (bi!=null) {
				bi.shutdown();
			}
		}
	}

}

