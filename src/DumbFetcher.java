import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class DumbFetcher {
	Map <Integer, Vertice> cache;
	Map <Vertice, Integer> inverseCache;
	PostgresConnector pgc;
	
	public DumbFetcher() throws ClassNotFoundException, SQLException{
		cache = new HashMap();
		inverseCache = new HashMap();
		pgc = new PostgresConnector();
	}
	
	public Vertice getVertice(int id){
		if(cache.containsKey(id))
			return cache.get(id);
		
		Vertice v = new Vertice();
		cache.put(id, v);
		inverseCache.put(v, id);
		
		return v;
	}
	
	public void loadDumbCache() throws SQLException{
		int count =0;
		for(int x=1;x<=265;x++){
			int uplimit = x*1000;
			int downlimit = (x-1)*1000; 
			
			String sql = "SELECT * from roads where j_from <"+uplimit+"AND j_from >= "+ downlimit;
		
			ResultSet rs = pgc.query(sql);
		
			while(rs.next()){

				int j_from = rs.getInt("j_from");
				int j_to = rs.getInt("j_to");

				getVertice(j_from).neighbours.add(getVertice(j_to));
			
			}
		}
		
	}
	
	public int BFS(int end){
		Deque <Vertice>q = new ArrayDeque();
		Set closed = new HashSet();
		
		Vertice initialVertice = cache.get(1);
		
		q.addLast(initialVertice);
		closed.add(initialVertice);
		
		while(!q.isEmpty()){
			
			Vertice tmp = q.pollFirst();
			
			if(inverseCache.get(tmp) == end)
				return inverseCache.get(tmp);
			
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
