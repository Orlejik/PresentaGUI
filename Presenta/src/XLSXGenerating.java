import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class XLSXGenerating {
    public void export(String currentDate) throws SQLException {
        String excelPath = "Presenta_"+currentDate+".xlsx";
        MySQLAccess.mysqlOperations();
        ArrayList<String> tablesList = ListOfTables.printTables();

    }
}

//26.01.2023
// TODO Develop the function of generating the EXCEL file
// TODO Function should insert the data in specific columns
// TODO The function should be executed on button press