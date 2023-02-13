import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

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
    static String date(String str, int id){
        String[] strings = str.split("_");
        return strings[id];
    }
    public static ArrayList<String> printTables() throws SQLException {
        MySQLAccess.mysqlOperations();
        ArrayList<String> tablesArray = new ArrayList<>();
        String query = "show tables";
        PreparedStatement prst = MySQLAccess.connection.prepareStatement(query);
        ResultSet resultSet1 = prst.executeQuery();
        while(resultSet1.next()){
            String table = resultSet1.getString("Tables_in_presenta");
            if(!date(table, 2).equals(MySQLAccess.getCurrentTime())) continue;
            tablesArray.add(table);
        }
        return tablesArray;
    }
    public static ArrayList<String> printTable(String collective, String tura) throws SQLException {
        MySQLAccess.mysqlOperations();
        ArrayList<String> listOfRecords = new ArrayList<>();
        String querry = "SELECT * FROM "+collective+"_"+tura+"_"+MySQLAccess.getCurrentTime();
        PreparedStatement prst = MySQLAccess.connection.prepareStatement(querry);
        ResultSet result = prst.executeQuery();
        while(result.next()){
            String id = result.getString("Id");
            String time = result.getString("time");
            String op_number = result.getString("op_number");
            String shift = result.getString("shift");
            listOfRecords.add(id+" "+time+" "+op_number+" "+shift);
        }
        return listOfRecords;
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
            //TOP Header CElls Style!
            Font fontTOPHeader = presentaWorkbook.createFont();
            CellStyle topHeaderStyle = presentaWorkbook.createCellStyle();
            fontTOPHeader.setBold(true);
            fontTOPHeader.setFontHeight((short)500);
            fontTOPHeader.setFontName("Calibri Light");
            topHeaderStyle.setWrapText(true);
            topHeaderStyle.setFont(fontTOPHeader);
            topHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            topHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
            //TOP Header CElls Style!
            //BOTTOM Header CElls Style!
            Font fontBOTTOMHeader = presentaWorkbook.createFont();
            CellStyle bottomHeaderStyle = presentaWorkbook.createCellStyle();
            fontBOTTOMHeader.setBold(true);
            fontBOTTOMHeader.setFontHeight((short)300);
            fontBOTTOMHeader.setFontName("Calibri Light");
            bottomHeaderStyle.setWrapText(true);
            bottomHeaderStyle.setFont(fontBOTTOMHeader);
            bottomHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            bottomHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
            //BOTTOM Header CElls Style!
            //Body Cells Style
            Font fontBodyCell = presentaWorkbook.createFont();
            CellStyle bodyCellStyle = presentaWorkbook.createCellStyle();
            fontBodyCell.setBold(false);
            fontBodyCell.setFontHeight((short)300);
            fontBodyCell.setFontName("Calibri Light");
            bodyCellStyle.setWrapText(true);
            bodyCellStyle.setFont(fontBodyCell);
            bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            //Body Cells Style
            HSSFSheet presentaSheet = presentaWorkbook.createSheet(date(table,0));
            HSSFRow rowHeadCollective = presentaSheet.createRow((short)0);
            Cell cell = rowHeadCollective.createCell(1);
            cell.setCellValue(date(table,0));
            cell.setCellStyle(topHeaderStyle);
            rowHeadCollective.setHeight((short)800);
            HSSFRow rowHead = presentaSheet.createRow((short)1);
            rowHead.setHeight((short) 300);
            presentaSheet.setColumnWidth(0, 3000);
            presentaSheet.setColumnWidth(1, 10000);
            presentaSheet.setColumnWidth(2, 5000);
            presentaSheet.setColumnWidth(3, 5000);
            Cell cellHeaderNumber = rowHead.createCell(0);
            cellHeaderNumber.setCellValue("Number");
            cellHeaderNumber.setCellStyle(bottomHeaderStyle);
            Cell cellHeaderTime = rowHead.createCell(1);
            cellHeaderTime.setCellValue("Time");
            cellHeaderTime.setCellStyle(bottomHeaderStyle);
            Cell cellHeaderOpNumber = rowHead.createCell(2);
            cellHeaderOpNumber.setCellValue("Operator Number");
            cellHeaderOpNumber.setCellStyle(bottomHeaderStyle);
            while(result.next()){
                String id = result.getString("Id");
                String time = result.getString("time");
                String op_number = result.getString("op_number");
                String shift = result.getString("shift");

                if(shift.equals("M")){
                    fullShift = "Morning";
                } else if (shift.equals("T")) {
                    fullShift = "Afternoon";
                }else{
                    fullShift ="Night";
                }
                Cell cell1 = rowHeadCollective.createCell(2);
                cell1.setCellValue(fullShift);
                cell1.setCellStyle(topHeaderStyle);
                HSSFRow row = presentaSheet.createRow(linesCounter);
                Cell cellID = row.createCell(0);
                cellID.setCellValue(id);
                cellID.setCellStyle(bodyCellStyle);
                Cell cellTime = row.createCell(1);
                cellTime.setCellValue(time);
                cellTime.setCellStyle(bodyCellStyle);
                Cell opeatorNumber = row.createCell(2);
                opeatorNumber.setCellValue(op_number);
                opeatorNumber.setCellStyle(bodyCellStyle);
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
