package algorithm;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Louvain implements Cloneable{
    int n; //节点数目
    int m; //边数目
    int cluster[]; //节点社区号
    public int cluster_n[];//社区节点数量数组
    public int cluster_v[];//社区边数量数组
    public int node_n[];//社区合并为节点后原来包含的节点个数
    public int node_v[];//如上
    int new_node_n[];
    int new_node_v[];
    Edge edge[]; 
    int head[]; 
    int top; 
    double resolution; //总权重
    double node_weight[]; 
    double totalEdgeWeight; 
    double[] cluster_weight; //社区内特定权重2*内部边+1*外部边 或解释为社区内节点设计涉及边的权重和（重复计算）
    double eps = 1e-14; // 影响限度
   public int global_n; // 等于n
   public int global_cluster[]; // 等于cluster[]
    Edge[] new_edge;
    int[] new_head;
    int new_top = 0;
    final int iteration_time = 1; // 迭代次数
    Edge global_edge[]; 
    int global_head[];
    int global_top=0;
    public Map<Integer,Long> index1 = new HashMap<>();
    public Map<Long,Integer> index2 = new HashMap<>();
    public enum nod implements Label{
    	oversea_enterprise,
    	domestic_enterprise
    }
    public enum rels implements RelationshipType{
    	paysum,
    	investsum
    }
    void addEdge(int u, int v, double weight) {
        if(edge[top]==null)
            edge[top]=new Edge();
        edge[top].v = v;
        edge[top].weight = weight;
        edge[top].next = head[u];
        head[u] = top++;
    }

    void addNewEdge(int u, int v, double weight) {
        if(new_edge[new_top]==null)
            new_edge[new_top]=new Edge();
        new_edge[new_top].v = v;
        new_edge[new_top].weight = weight;
        new_edge[new_top].next = new_head[u];
        new_head[u] = new_top++;
    }
    void addGlobalEdge(int u, int v, double weight) {
        if(global_edge[global_top]==null)
            global_edge[global_top]=new Edge();
        global_edge[global_top].v = v;
        global_edge[global_top].weight = weight;
        global_edge[global_top].next = global_head[u];
        global_head[u] = global_top++;
    }
    public GraphDatabaseService gb= null;
    public void init() {
        try {
            gb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("D:\\neo4j-community-3.4.0\\data\\databases\\g.db"));
            Transaction tx= gb.beginTx();
          
            int sum = 0;
            int sum_ = 0;
            ResourceIterable<Relationship> relationships = gb.getAllRelationships();
            ResourceIterator<Relationship> relationshipResourceIterator = relationships.iterator();
            while (relationshipResourceIterator.hasNext()) {
                Relationship rela = relationshipResourceIterator.next();
                if ((rela.getType().name().equals("paysum"))||(rela.getType().name().equals("investsum"))) {
                	sum++;
                	if (!index2.containsKey(rela.getStartNode().getId())) {
                		index2.put(rela.getStartNode().getId(), sum_);
                		index1.put(sum_, rela.getStartNode().getId());
                		sum_++;
                	}
                	if (!index2.containsKey(rela.getEndNode().getId())) {
                		index2.put(rela.getEndNode().getId(), sum_);
                		index1.put(sum_, rela.getEndNode().getId());
                		sum_++;
                	}
                }
            }
            m = sum;
            m *= 2;
            edge = new Edge[m];
            global_n = n = sum_;
            head = new int[n];
            for (int i = 0; i < n; i++)
                head[i] = -1;
            top = 0;
            global_edge=new Edge[m];
            global_head = new int[n];
            for(int i=0;i<n;i++)
                global_head[i]=-1;
            global_top=0;
            global_cluster = new int[n];
            for (int i = 0; i < global_n; i++)
                global_cluster[i] = i;
            node_weight = new double[n];
            totalEdgeWeight = 0.0;


            relationships = gb.getAllRelationships();
            relationshipResourceIterator = relationships.iterator();
            while (relationshipResourceIterator.hasNext()) {
                Relationship relationship = relationshipResourceIterator.next();
                if ((relationship.getType().name().equals("paysum"))||(relationship.getType().name().equals("investsum"))) {
                	long u = relationship.getStartNode().getId();
                	long v = relationship.getEndNode().getId();
                	double curw;
                	Object s = relationship.getProperty("weight");
                	curw = (int)s;
                	if (curw > 100) 
                		curw = 100;
                	addEdge(index2.get(u), index2.get(v), curw);
                	addEdge(index2.get(v), index2.get(u), curw);
                	addGlobalEdge(index2.get(u), index2.get(v), curw);
                	addGlobalEdge(index2.get(v), index2.get(u), curw);
                	totalEdgeWeight += 2 * curw;
                	node_weight[index2.get(u)] += curw;
                	if (u != v) {
                		node_weight[index2.get(v)] += curw;
                	}
                }
            }
            resolution = 1 / totalEdgeWeight;
            gb.shutdown();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    void init_node(){

        node_n = new int[n];
        node_v = new int[n];
        for (int i = 0; i < n; i++) { 

            node_n[i] = 1;
            node_v[i] = 0;
        }

    }
    void init_cluster() {
        cluster = new int[n];
        cluster_n = new int [n];
        cluster_v = new int[n];
        for (int i = 0; i < n; i++) { 
            cluster[i] = i;
            cluster_n[i] = node_n[i];
            cluster_v[i] = node_v[i];
        }
    }

    boolean try_move_i(int i) { 
        double[] edgeWeightPerCluster = new double[n];
        for (int j = head[i]; j != -1; j = edge[j].next) {
            int l = cluster[edge[j].v]; 
            edgeWeightPerCluster[l] += edge[j].weight;
        }
        int bestCluster = -1; 
        double maxx_deltaQ = 0.0; 
        double s = edgeWeightPerCluster[cluster[i]];
        boolean[] vis = new boolean[n];
        cluster_weight[cluster[i]] -= node_weight[i];
        cluster_n[cluster[i]] -= node_n[i];
//        cluster_v[cluster[i]] -= edgeWeightPerCluster[cluster[i]];
//        System.out.print(edgeWeightPerCluster[cluster[i]]);
        cluster_v[cluster[i]] -= node_v[i];
        for (int j = head[i]; j != -1; j = edge[j].next) {
            int l = cluster[edge[j].v];
            if (vis[l]) 
                continue;
            vis[l] = true;
            double cur_deltaQ = edgeWeightPerCluster[l];
            cur_deltaQ -= node_weight[i] * cluster_weight[l] * resolution;
            double count = (cluster_v[l]+edgeWeightPerCluster[l]+node_v[i])/((cluster_n[l]+node_n[i])*(cluster_n[l]+node_n[i]-1)/2);
            if ((cur_deltaQ > maxx_deltaQ)&&(count>=4)) {
                bestCluster = l;
                maxx_deltaQ = cur_deltaQ;
                s = edgeWeightPerCluster[l];
            }
            edgeWeightPerCluster[l] = 0;
        }
        if (maxx_deltaQ < eps) {
            bestCluster = cluster[i];
        }
         //System.out.println(maxx_deltaQ);
        cluster_weight[bestCluster] += node_weight[i];
        cluster_v[bestCluster] += s;
        cluster_v[bestCluster] += node_v[i];
        cluster_n[bestCluster] += node_n[i];
        if (bestCluster != cluster[i]) {
            cluster[i] = bestCluster;
            return true;
        }
        return false;
    }

    void rebuildGraph() { 
        
        int[] change = new int[n];
        int change_size=0;
        boolean vis[] = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (vis[cluster[i]])
                continue;
            vis[cluster[i]] = true;
            change[change_size++]=cluster[i];
        }
        int[] index = new int[n];
        int[] ind = new int[n];
        for (int i = 0; i < change_size; i++) {
            index[change[i]] = i;
            ind[i] = change[i];
        }

        int new_n = change_size; 
        new_node_v = new int[new_n];
        for (int i = 0; i < change_size; i++)
            new_node_v[i] = cluster_v[ind[i]];
        new_node_n = new int[new_n];
        new_edge = new Edge[m];
        new_head = new int[new_n];
        new_top = 0;
        double new_node_weight[] = new double[new_n]; 
        for(int i=0;i<new_n;i++)
            new_head[i]=-1;
        
        ArrayList<Integer>[] nodeInCluster = new ArrayList[new_n];
        for (int i = 0; i < new_n; i++)
            nodeInCluster[i] = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            nodeInCluster[index[cluster[i]]].add(i);
        }
        for (int u = 0; u < new_n; u++) { 
            boolean visindex[] = new boolean[new_n]; 
            double delta_w[] = new double[new_n]; 
            new_node_n[u] = nodeInCluster[u].size();
            for (int i = 0; i < nodeInCluster[u].size(); i++) {
                int t = nodeInCluster[u].get(i);
                for (int k = head[t]; k != -1; k = edge[k].next) {
                    int j = edge[k].v;
                    int v = index[cluster[j]];
                    if (u != v) {
                        if (!visindex[v]) {
                            addNewEdge(u, v, 0);
                            visindex[v] = true;
                        }
                        delta_w[v] += edge[k].weight;
                    }
                }
                new_node_weight[u] += node_weight[t];
            }
            for (int k = new_head[u]; k != -1; k = new_edge[k].next) {
                int v = new_edge[k].v;
                new_edge[k].weight = delta_w[v];
            }
        }

        // 鏇存柊绛旀
        int[] new_global_cluster = new int[global_n];
        for (int i = 0; i < global_n; i++) {
            new_global_cluster[i] = index[cluster[global_cluster[i]]];
        }
        for (int i = 0; i < global_n; i++) {
            global_cluster[i] = new_global_cluster[i];
        }
        top = new_top;
        for (int i = 0; i < m; i++) {
            edge[i] = new_edge[i];
        }
        for (int i = 0; i < new_n; i++) {
            node_weight[i] = new_node_weight[i];
            node_v[i] = new_node_v[i];
            node_n[i] = new_node_n[i];
            head[i] = new_head[i];
        }
        n = new_n;
        init_cluster();
    }

    void print() {
        for (int i = 0; i < global_n; i++) {
            System.out.println(i + ": " + global_cluster[i]);
        }
        System.out.println("-------");
    }

    public void louvain() {
        init_node();
        init_cluster();

        int count = 0; //迭代次数
        boolean update_flag; 
        do { 
        
            count++;
            cluster_weight = new double[n];
            for (int j = 0; j < n; j++) { 
                cluster_weight[cluster[j]] += node_weight[j];
            }
            int[] order = new int[n]; 
            for (int i = 0; i < n; i++)
                order[i] = i;
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                int j = random.nextInt(n);
                int temp = order[i];
                order[i] = order[j];
                order[j] = temp;
            }
            int enum_time = 0; 
            int point = 0; 
            update_flag = false; 
            do {
                int i = order[point];
                point = (point + 1) % n;
                if (try_move_i(i)) { 
                    enum_time = 0;
                    update_flag = true;
                } else {
                    enum_time++;
                }
            } while (enum_time < n);
            if (count > iteration_time || !update_flag) 
                break;
            rebuildGraph(); 
        } while (true);               
    }
    
    public double[] gettightness() {//返回社区划分完成后的紧密度
    	double[] tight = new double[global_n];
    	for(int i=0;i<global_n;i++) {
    		double[] edgeWeightPerCluster = new double[n];
            for (int j = head[i]; j != -1; j = edge[j].next) {
                int l = cluster[edge[j].v]; 
                edgeWeightPerCluster[l] += edge[j].weight;
                tight[i]=(cluster_v[l]+edgeWeightPerCluster[l]+node_v[i])/((cluster_n[l]+node_n[i])*(cluster_n[l]+node_n[i]-1)/2);
            }    		
    	}
    	return tight;
    }
}
