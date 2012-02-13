import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Cache {

	int top;
	Vector <Vertice> memorySensitiveLayout;
	Map <Integer, Integer>idIndexMap;
	
	public Cache(int size){
		int top =0;
		
		idIndexMap = new HashMap();
		memorySensitiveLayout = new Vector(size);

		int x=0;
		while(x<size){
			
			memorySensitiveLayout.add(x, new Vertice());
			x++;
		}
	}
	
	public void addNeighbour(int idOfParent, int idOfChild){
		
		int indexOfParent = getIndex(idOfParent);
		Vertice parent = memorySensitiveLayout.get(indexOfParent);

			
		int indexOfChild = getIndex(idOfChild)	;	
		
		Vertice child = memorySensitiveLayout.get(indexOfChild);
		parent.neighbours.add(child);
			
		
	}
	
	public Vertice getVerticeById(int id){
		
		int index = getIndex(id);
		
		return memorySensitiveLayout.get(index);
	}
	
	public Vertice setRoot(int rootID){
		
		int rootIndex = getIndex(rootID);
		
		
		return memorySensitiveLayout.get(rootIndex);
	}
	
	public int getIndex(int id){
		
		if(idIndexMap.containsKey(id)){
			return idIndexMap.get(id);
		}		
		
		idIndexMap.put(id, top);
		top++;
		
		return top-1;
	}
}
