import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
public class ListOfTables {
    public static Connection connection = MySQLAccess.connection;
    public static int tablesLength;
    static {
        try {
            tablesLength = printTables().size();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public static void printTable(String table) throws SQLException {
        MySQLAccess.mysqlOperations();
        String querry = "SELECT * FROM "+table;
        PreparedStatement prst = MySQLAccess.connection.prepareStatement(querry);
        ResultSet result = prst.executeQuery();
        while(result.next()){
            String id = result.getString("Id");
            String time = result.getString("time");
            String op_number = result.getString("op_number");
            String shift = result.getString("shift");

            System.out.printf("%s %s %s %s%n", id, time, op_number, shift);
        }
    }
    public static void executeTables() throws SQLException {
        String curDate = MySQLAccess.getCurrentTime();
        String[] tables = printTables().toArray(new String[0]);
        MySQLAccess.mysqlOperations();
        int linesCounter = 2;
        String fullShift = "";
        String userName = System.getProperty("user.name");
        String fileName = "C:\\Users\\"+userName+"\\Desktop\\Presenta_"+curDate+".xls";
        HSSFWorkbook presentaWorkbook = new HSSFWorkbook();
        for (int i = 0; i < tables.length; i++) {
            String table = tables[i];
            String querry = "SELECT * FROM "+table;
            PreparedStatement prst = MySQLAccess.connection.prepareStatement(querry);
            ResultSet result = prst.executeQuery();
            HSSFSheet presentaSheet = presentaWorkbook.createSheet(table);
            HSSFRow rowHeadCollective = presentaSheet.createRow((short)0);
            rowHeadCollective.createCell(1).setCellValue(table);
            HSSFRow rowHead = presentaSheet.createRow((short)1);
            rowHead.createCell(0).setCellValue("Number");
            rowHead.createCell(1).setCellValue("Time");
            rowHead.createCell(2).setCellValue("Operator Number");
            while(result.next()){
                String id = result.getString("Id");
                String time = result.getString("time");
                String op_number = result.getString("op_number");
                String shift = result.getString("shift");

                if(shift == "M"){
                    fullShift = "Morning";
                } else if (shift=="T") {
                    fullShift = "Afternoon";
                }else{
                    fullShift ="Night";
                }
                rowHeadCollective.createCell(2).setCellValue(fullShift);
                HSSFRow row = presentaSheet.createRow(linesCounter);
                row.createCell(0).setCellValue(id);
                row.createCell(1).setCellValue(time);
                row.createCell(2).setCellValue(op_number);

                linesCounter++;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                presentaWorkbook.write(fileOutputStream);


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            linesCounter=2;
        }
        System.out.println("File written fine!");
        try {
            presentaWorkbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
