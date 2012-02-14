import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;


public class Fetcher {

	Cache c;
	PostgresConnector pgc;
	Set closed;
	
	HashMap <Integer, Vector>rawGraph;
	
	public Fetcher(int size) throws ClassNotFoundException, SQLException{
		closed = new HashSet();
		c = new Cache(size);								
		pgc = new PostgresConnector();
		
		rawGraph = new HashMap();
		
		for(int i=0; i<size; i++)
			rawGraph.put(i,new Vector());
		
		for(int x=1;x<=size/1000;x++){
			int uplimit = x*1000;
			int downlimit = (x-1)*1000; 
			
			String sql = "SELECT * from p2 where j_from <"+uplimit+"AND j_from >= "+ downlimit;
		
			ResultSet rs = pgc.query(sql);
		
			while(rs.next()){

				int j_from = rs.getInt("j_from");
				int j_to = rs.getInt("j_to");

				rawGraph.get(j_from).add(j_to);
			}
		}
	}
	
	public void loadFetcher(int id, int l1, int l2) throws SQLException{
		
		double [] s = new double [3];
		
		s[0] = l1;
		s[1] = l2;
		s[2] = Double.POSITIVE_INFINITY;		
		
		Deque <Integer> [] roots = new ArrayDeque [3];
		Deque <Integer> [] leaves = new ArrayDeque [3];
		double [] space = new double [3];
		
		for(int x=0; x<3; x++){
			roots[x] = new ArrayDeque();
			leaves[x] = new ArrayDeque();
		}
		
		int initialVertice = id;
		
		int level = 2;
		
		roots[2].offerLast(initialVertice);
		
		while(true){											// assumes infinite memory
		//	System.out.println(leaves[level]+" "+level);

			if(roots[level].isEmpty()){
				
				roots[level].addAll(leaves[level]);
				leaves[level].clear();							//needs review
			
				if(space[level] >= s[level] ){
					leaves[level+1].addAll(roots[level]); 		//needs review
					roots[level].clear();
					space[level+1] += space[level];
					level++;
					continue;
				}
			}
			
			if(roots[level].isEmpty()){
				
				if(level == 2){
					break;
				}
				else{
					space[level+1] += space[level];
					level++;
				}
			}
			else{
				int v = roots[level].pollFirst();
				
				if(level>0){
					roots[level-1].offerLast(v);
					space[level-1] = 0;
					level--;
				}
				else{
					leaves[0].addAll(getChildrenOfVertice(v));
					space[0] += getNumberOfChildren(v);					
				}
			}
		}
		
		c.prune();
	}
	
	public int getNumberOfChildren(int v){

		return rawGraph.get(v).size();
		
	}

	public Vector getChildrenOfVertice(int v) throws SQLException{
		
		
		int j_from = v;
		int j_to;
		
		Vector neighboursToExplore = new Vector();
		Vector children = new Vector();
		Iterator <Integer>it = rawGraph.get(j_from).listIterator();
		
		while(it.hasNext()){
			int child = it.next();
			children.add(child);
			
			if(!closed.contains(child)){
				closed.add(child);
				neighboursToExplore.add(child);
			}
		}
		
		c.assignNeighbour(j_from, children);
		return neighboursToExplore;
	}
	
	public int BFS(int start, int end){
		Deque <Integer>q = new ArrayDeque();
		Set visited = new HashSet();
		
		int initialVertice = 0;
		int tmp = 1;
		
		q.addLast(initialVertice);
		visited.add(initialVertice);
		
		while(!q.isEmpty()){

			tmp = q.pollFirst();

			//can have an exit condition here
			Vector <Integer>neighbours = c.getNeighbours(tmp);

			for(int x=0; x<neighbours.size(); x++){
				int o = neighbours.get(x);

				if(!visited.contains(o)){
					visited.add(o);
					q.addLast(o);
				}
			}

		}
		
		return closed.size();
	}
	
}


/*old code with get as you go queries
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;


public class Fetcher {

	Map <Vertice, Integer> verticeIDMap;				//needed when want to load neighbours
	Cache c;
	PostgresConnector pgc;
	
	public Fetcher(int size) throws ClassNotFoundException, SQLException{
		verticeIDMap = new HashMap();
		c = new Cache(size);								//feeding 3 as default node degree
		pgc = new PostgresConnector();
	}
	
	public void loadFetcher(int id, int l1, int l2) throws SQLException{
		
		double [] s = new double [3];
		
		s[0] = l1;
		s[1] = l2;
		s[2] = Double.POSITIVE_INFINITY;		
		
		Deque <Vertice> [] roots = new ArrayDeque [3];
		Deque <Vertice> [] leaves = new ArrayDeque [3];
		double [] space = new double [3];
		
		for(int x=0; x<3; x++){
			roots[x] = new ArrayDeque();
			leaves[x] = new ArrayDeque();
		}
		
		Vertice initialVertice = setRoot(id);
		
		int level = 2;
		
		roots[2].offerLast(initialVertice);
		
		while(true){											// assumes infinite memory
		//	System.out.println(leaves[level]+" "+level);

			if(roots[level].isEmpty()){
				
				roots[level].addAll(leaves[level]);
				leaves[level].clear();						//needs review
			
				if(space[level] >= s[level] ){
					leaves[level+1].addAll(roots[level]); 		//needs review
					roots[level].clear();
					space[level+1] += space[level];
					level++;
					continue;
				}
			}
			
			if(roots[level].isEmpty()){
				
				if(level == 2){
					break;
				}
				else{
					space[level+1] += space[level];
					level++;
				}
			}
			else{
				Vertice v = roots[level].pollFirst();
				
				if(level>0){
					roots[level-1].offerLast(v);
					space[level-1] = 0;
					level--;
				}
				else{
					loadVerticeToCache(v);
					leaves[0].addAll(getChildrenOfVertice(v));
					space[0] += v.neighbours.capacity()*4;		//needs review 
				}
			}
		}
	}
	
	public Vertice setRoot(int id){
		
		Vertice v = c.setRoot(id);
		
		verticeIDMap.put(v, id);
		
		return v;
		
	}
	
	public Vertice getVertice(int id){
		Vertice v = c.getVerticeById(id);
		verticeIDMap.put(v, id);
		
		return v;
	}

	public void loadVerticeToCache(Vertice v){
		c.memorySensitiveLayout.add(v);
	}
	
	public Deque getChildrenOfVertice(Vertice v) throws SQLException{
		
		Deque <Vertice>neighboursToExplore = new ArrayDeque();
		
		int j_from = verticeIDMap.get(v);
		String sql = "Select j_to from p2 WHERE j_from = " +j_from+";";
		
		ResultSet rs = pgc.query(sql);
		
		//int [] neighbours = new int [rs.getFetchSize()];
		int x=0;
		
		while(rs.next()){
			
			int toID = rs.getInt("j_to");
			
			c.addNeighbour(j_from, toID);

			
			if(!verticeIDMap.containsValue(toID))	{		// this saves memory explosion at the cost of lower locality ie- not all neighbouring vertices will be closely laid-out to fetch
				
				Vertice toVertice = getVertice(toID);
				neighboursToExplore.addLast(getVertice(toID));
			
			}			
		}

		return neighboursToExplore;
	}
	
	public int BFS(int end){
		Deque <Vertice>q = new ArrayDeque();
		Set closed = new HashSet();
		
		Vertice initialVertice = c.memorySensitiveLayout.firstElement();
		
		q.addLast(initialVertice);
		closed.add(initialVertice);
		
		while(!q.isEmpty()){
			
			Vertice tmp = q.pollFirst();
			
			if(verticeIDMap.get(tmp) == end)
				return verticeIDMap.get(tmp);
			
			for(int x =0; x<tmp.neighbours.size(); x++){
				Vertice o = tmp.neighbours.get(x);
				if(!closed.contains(o)){
					closed.add(o);
					q.addLast(o);
				}
			}
		}
		
		return 0;
	}
	
}

 */
