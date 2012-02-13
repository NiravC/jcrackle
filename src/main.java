import java.sql.SQLException;


public class main {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		System.out.println("Loading fetcher...");

		long startTime = System.nanoTime();	

		Fetcher f = new Fetcher(10000);
		f.loadFetcher(0, 64, 4096);
		
		
		long stopTime = System.nanoTime();
		long duration = stopTime - startTime;
		
		System.out.println("Loading duration: "+duration);
		
		System.out.println("Running BFS...");
		
		
		startTime = System.nanoTime();		
		
		int end = f.BFS(9999);

		stopTime = System.nanoTime();
		duration = stopTime - startTime;
		
		System.out.println("BFS duration: "+duration);
		
		System.out.println(end);
		
	}

}
