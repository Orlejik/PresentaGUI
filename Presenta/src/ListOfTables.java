import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListOfTables {

    public static int tablesLength;
    static {
        try {
            tablesLength = printTables().size();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ListOfTables() throws SQLException {
    }
    public static ArrayList<String> printTables() throws SQLException {
        MySQLAccess.mysqlOperations();
        ArrayList<String> tablesArray = new ArrayList<>();
        String query = "show tables";
        PreparedStatement prst = MySQLAccess.connection.prepareStatement(query);
        ResultSet resultSet1 = prst.executeQuery();
        while(resultSet1.next()){
            tablesArray.add(resultSet1.getString("Tables_in_presenta"));
        }
        return tablesArray;
    }
}
