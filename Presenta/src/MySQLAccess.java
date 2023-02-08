import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class MySQLAccess {
    private static ResultSet rs = null;
    public static Connection connection = null;
    static PreparedStatement prst = null;
    static String query = "";
    public static String getCurrentTime() {
        java.util.Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("ddMMyyyy");
        String currentTime = dtf.format(date);
        return currentTime;
    }
    public static String getCurrentTime_db() {
        java.util.Date date = new Date();
        SimpleDateFormat dft_db = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");
        String currentTime_db = dft_db.format(date);
        return currentTime_db;
    }
    public static void mysqlOperations() {
        try {
            String dbURL = "jdbc:mysql://10.20.193.237:3306/presenta?useSSL=false";
            String userName = "artiom";
            String passwd = "admin";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try {
                connection = DriverManager.getConnection(dbURL, userName, passwd);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void createTable(Connection con, String collective, String tura) throws SQLException {
        query = "CREATE TABLE if not exists "+collective+"_"+tura+"_"+getCurrentTime()+" (Id int AUTO_INCREMENT, time varchar(255), op_number varchar(255), shift varchar(255), primary key(id));";
        prst = con.prepareStatement(query);
        prst.execute();
    }
    public static void makeRecord(Connection con, String collective, String op_number, String tura) throws SQLException {
        query = "INSERT INTO "+collective+"_"+tura+"_"+getCurrentTime()+"(time, op_number, shift ) VALUES ('"+getCurrentTime_db()+"', '"+op_number+"', '"+tura+"');";
        prst = con.prepareStatement(query);
        prst.execute();
    }
    public static void createDBTable(String collective, String op_number, String tura) throws SQLException, InterruptedException {
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, ""+collective+"_"+tura+"_"+getCurrentTime()+"", null);
        if (!tables.next()) {
            createTable(connection, collective, tura);
            makeRecord(connection, collective, op_number, tura);
        }
        else {
            makeRecord(connection, collective, op_number, tura);
        }
    }
    public static List<String> showResults(Connection con, String collective, String tura) throws SQLException {
        List<String> listOfItems = new ArrayList<>();
        String showTableQuery = "SELECT * FROM "+collective+"_"+tura+"_"+getCurrentTime();
        PreparedStatement preparedStatement =con.prepareStatement(showTableQuery);
        ResultSet resultSet = preparedStatement.executeQuery(showTableQuery);

        while(resultSet.next()){
            listOfItems.add(resultSet.getInt("Id") + " " +resultSet.getString("time") + " " +resultSet.getString("op_number") + " " + resultSet.getString("shift"));
        }
        return listOfItems;
    }
    public void closeDBConnection() throws SQLException {
        connection.close();
    }
    public boolean isDBConnected(Connection connection) throws SQLException {
        return connection != null && connection.isClosed();
    }
}

