import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) {
        try{
            String fileName = "C:\\Test.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("January");
            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.createCell(0).setCellValue("S.Number");
            rowhead.createCell(1).setCellValue("Customer Name");
            rowhead.createCell(2).setCellValue("Account number");
            rowhead.createCell(3).setCellValue("E-Mail");
            rowhead.createCell(4).setCellValue("Balance");

            HSSFRow row = sheet.createRow((short)1);

            row.createCell(0).setCellValue("1");
            row.createCell(1).setCellValue("Artiom Oriol");
            row.createCell(2).setCellValue("9999999");
            row.createCell(3).setCellValue("artiom.oriol@mail.com");
            row.createCell(4).setCellValue("70000.00");

            HSSFRow row1 = sheet.createRow((short)2);

            row1.createCell(0).setCellValue("1");
            row1.createCell(1).setCellValue("Irina Oriol");
            row1.createCell(2).setCellValue("8888888");
            row1.createCell(3).setCellValue("irina.oriol@mail.com");
            row1.createCell(4).setCellValue("120000.00");


            FileOutputStream fileOut= new FileOutputStream(fileName);
            workbook.write(fileOut);

            fileOut.close();

            workbook.close();

            System.out.println("Excel File was generated!!");



        }catch(Exception ex){
            throw new RuntimeException();
        }
    }
}
