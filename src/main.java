import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
        BufferedReader in = new BufferedReader (new FileReader("/Users/Chavda/Downloads/Snap/examples/graphgen/o1.txt"));
        String strLine;
        Connector c = new Connector();
     

        while((strLine = in.readLine()) != null)
        {
                      
        	if(strLine.charAt(0) == '#')
        		continue;

            else{
            	String [] tmp = strLine.split("\t");
            	String sql = "INSERT INTO p2 values ("+tmp[0]+","+tmp[1]+");";
            	c.query(sql);
            }
                      
        }
	}
}
