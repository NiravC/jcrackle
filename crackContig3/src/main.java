import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Vector;


public class main {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	/*ByteBuffer  x = ByteBuffer.allocate(20);
	
	x.putInt(5);
	x.putInt(5);
	x.putInt(5);
	x.putInt(x.position()-4, 6);
	System.out.println(x.position());

	x.position(0);
	x.getInt();
	x.getInt();
*/
		Fetcher f = new Fetcher (30000);
		f.loadFetcher(0, 64, 4096);
		
		
		long startTime = System.nanoTime();		
		
		int end = f.BFS(0, 9999);

		long stopTime = System.nanoTime();
		long duration = stopTime - startTime;
		
		System.out.println("BFS duration: "+duration);
		System.out.println(end);
	}

}
