import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Cache {

	HashMap <Integer, Integer> idPositionMap;
	ByteBuffer edgeList;
	int currentID;
	int top;
	
	public Cache(int size){
		top =0;
		idPositionMap = new HashMap();
		edgeList = ByteBuffer.allocate(4*size*4);
		currentID = -1;
	}
	
	public void assignNeighbour(int parentID, Vector <Integer>childrenID){
		int currentPosition = edgeList.position();

		idPositionMap.put(parentID, currentPosition);
		
		int childrenNumber = childrenID.size();

		
		edgeList.putInt(childrenNumber);
		
		for(int x=0; x<childrenNumber; x++){
			edgeList.putInt(childrenID.get(x));
		}
		top = edgeList.position();
	}
 
	public void prune(){
		
	/*	edgeList.position(0);
		int pointer = 0;
		
		while(pointer < top){
			int childrenNumber = edgeList.getInt(pointer);
			pointer +=4;
			pointer +=4*childrenNumber;
			System.out.println(childrenNumber);

			}
			
		*/
		
		int bufferPointer =0;
		int childPointer;
		edgeList.position(0);
		int childrenNumber;
		int count=0;
		
		while(bufferPointer < top){
			 childrenNumber = edgeList.getInt();

			
				for(int x=0; x<childrenNumber; x++){
				childPointer = edgeList.position();
				int childID = edgeList.getInt();

				edgeList.putInt(childPointer, idPositionMap.get(childID));

				
			}
			
			bufferPointer = edgeList.position();
		}
		
	}
	
	public Vector getNeighbours(int position){
		edgeList.position(position);
		
		int childrenNumber = edgeList.getInt();

		Vector children = new Vector(childrenNumber);
		
		while((edgeList.position()-(position+4))/4 < childrenNumber)
			children.add(edgeList.getInt());
		
		return children;
	}
	
	public int getNeighboursNum(int position){
		edgeList.position(position);
		
		return edgeList.getInt();
	}
}
