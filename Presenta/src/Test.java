import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test {
    public static ArrayList<String> printTables() throws SQLException {
        MySQLAccess.mysqlOperations();
        ArrayList<String> tablesArray = new ArrayList<>();
        String query = "show tables";
        PreparedStatement prst = MySQLAccess.connection.prepareStatement(query);
        ResultSet resultSet1 = prst.executeQuery();
        while(resultSet1.next()){
            String table = resultSet1.getString("Tables_in_presenta");
            if(!date(table).equals(MySQLAccess.getCurrentTime())) continue;
            tablesArray.add(table);
        }
        return tablesArray;
    }
    static String date(String str){
        String[] strings = str.split("_");
        return strings[2];
    }
    public static void main(String[] args) throws SQLException {
        System.out.println(printTables());
    }
}
