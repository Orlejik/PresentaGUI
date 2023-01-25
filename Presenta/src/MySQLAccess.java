import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MySQLAccess {
    private static ResultSet rs = null;
    private static SimpleDateFormat dtf = new SimpleDateFormat("ddMMyyyy");
    private  static SimpleDateFormat dft_db = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
    private static java.util.Date date = new Date();
    static String currentTime = dtf.format(date);
    static String currentTime_db = dft_db.format(date);
    static Connection connection = null;
    static PreparedStatement prst = null;
    String query = "";
    public void mysqlOperations() {
        try {
            String dbURL = "jdbc:mysql://10.20.193.237:3306/presenta?useSSL=false";
            String userName = "artiom";
            String passwd = "admin";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try {
                connection = DriverManager.getConnection(dbURL, userName, passwd);
                System.out.println("Connection successful");
                connection.setAutoCommit(false);
                connection.commit();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void createTable(Connection con, String collective, String tura) throws SQLException {
        query = "CREATE TABLE if not exists "+collective+"_"+tura+"_"+currentTime+" (Id int AUTO_INCREMENT, time varchar(255), op_number varchar(255), shift varchar(255), primary key(id));";
        prst = con.prepareStatement(query);
        prst.execute();
    }
    public void makeRecord(Connection con, String collective, String op_number, String tura) throws SQLException {
        query = "INSERT INTO "+collective+"_"+tura+"_"+currentTime+"(time, op_number, shift ) VALUES ('"+currentTime_db+"', '"+op_number+"', '"+tura+"');";
        prst = con.prepareStatement(query);
        prst.execute();
    }
    public void createDBTable(String collective, String op_number, String tura) throws SQLException, InterruptedException {
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, ""+collective+"_"+tura+"_"+currentTime+"", null);
        if (!tables.next()) {
            createTable(connection, collective, tura);
            makeRecord(connection, collective, op_number, tura);
            System.out.println("Created and record!");
        }
        else {
            createTable(connection, collective, tura);
            System.out.println("Just Record");
        }
    }
    public void closeDBConnection() throws SQLException {
        connection.close();
        if(isDBConnected(connection)){
            System.out.println("Connection is closed");
        }else{
            System.out.println("Connection is still opened");
        }
    }
    public boolean isDBConnected(Connection connection) throws SQLException {
        return connection != null && connection.isClosed();
    }
}

