import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Test {
    public int tablesLength = (printTables(MySQLAccess.connection).size());

    public Test() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {
        String username = System.getProperty("user.name");
        System.out.println(username);
        MySQLAccess.mysqlOperations();
        System.out.println(printTables(MySQLAccess.connection));
        System.out.println(printTables(MySQLAccess.connection).size());
    }
    public static ArrayList<String> printTables(Connection conn) throws SQLException {
        ArrayList<String> tablesArray = new ArrayList<>();
        String query = "show tables";
        PreparedStatement prst = conn.prepareStatement(query);
        ResultSet resultSet1 = prst.executeQuery();
        while(resultSet1.next()){
            tablesArray.add(resultSet1.getString("Tables_in_presenta"));
        }
        return tablesArray;
    }
}
