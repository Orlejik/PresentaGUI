import org.apache.poi.hssf.usermodel.DummyGraphics2d;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
public class PresentAGui extends JFrame {
    public static JPanel panel1;
    public static JLabel label1;
    public static String clearNumber(String code){
        if(code.toUpperCase().startsWith("O000")){
            code = code.toUpperCase().replace("O000", "");
        } else if (code.toUpperCase().startsWith("O00")) {
            code = code.toUpperCase().replace("O00", "");
        }else if (code.toUpperCase().startsWith("O0")) {
            code = code.toUpperCase().replace("O0", "");
        }
        return code;
    }
    public PresentAGui() throws SQLException {
        super("Presenta");
        createGUI();
    }
    public ArrayList<String> showCollectives() throws SQLException{
        MySQLAccess.mysqlCollectives();
        ArrayList<String> collectives =new ArrayList<>();

        String querry = "SELECT * FROM collectives";
        PreparedStatement stat = MySQLAccess.connection.prepareStatement(querry);
        ResultSet resultSet = stat.executeQuery();
        while(resultSet.next()){
            collectives.add(resultSet.getString("Collective"));
        }
        return collectives;
    }
    public void createGUI() throws SQLException {
        MySQLAccess mySQLAccess = new MySQLAccess();
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        начало позиционирования шапки
        panel1 = new JPanel();
        panel1.setLayout(null);
        label1= new JLabel("Op. Number", SwingConstants.CENTER);
        JLabel label2= new JLabel("Col. Number", SwingConstants.CENTER);
        JLabel label3 = new JLabel("Shift", SwingConstants.CENTER);
        JLabel lableLog = new JLabel("", SwingConstants.CENTER);
        JTextField text1 = new JTextField();
        JComboBox collectives = new JComboBox();
        JComboBox shifts = new JComboBox();
        JTextArea textArea =new JTextArea();
        textArea.setEditable(false);
        JButton button = new JButton("Finish");
        collectives.addItem("---");
        for (String coll : showCollectives()) {
            collectives.addItem(coll);
        }
        shifts.addItem("M");
        shifts.addItem("T");
        shifts.addItem("N");
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        Font TextFieldsFont = new Font("Calibri Light", Font.BOLD, 22);
        Font textAreaFont = new Font("Calibri Light", Font.BOLD, 21);
        label1.setBounds(5,5,196,25);
        label2.setBounds(395, 5, 196,25);
        label3.setBounds(200, 5, 197, 25);
        text1.setBounds(5,40,196,35);
        shifts.setBounds(245, 40, 100, 35);
        collectives.setBounds(395,40,196,35);
        text1.setBorder(BorderFactory.createCompoundBorder(text1.getBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        text1.setFont(TextFieldsFont);
        collectives.setFont(TextFieldsFont);
        shifts.setFont(TextFieldsFont);
        lableLog.setBounds(172, 975,265,25);
        lableLog.setBorder(border);
        textArea.setBounds(5,85,585, 820);
        textArea.setBorder(BorderFactory.createCompoundBorder(text1.getBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        textArea.setFont(textAreaFont);
        button.setBounds(225,925,160,40);
        button.setFont(TextFieldsFont);
        panel1.add(label1);
        panel1.add(label2);
        panel1.add(text1);
        panel1.add(label3);
        panel1.add(lableLog);
        panel1.add(shifts);
        panel1.add(collectives);
        panel1.add(textArea);
        panel1.add(button);
        lableLog.setText("Powered by Oriol Artiom");
        lableLog.setFont(new Font("Calibri Light", Font.PLAIN, 10));
        lableLog.setForeground(Color.gray);
//        Конец позиционирования шапки
//        начало включения элементов в тело самой формы
        getContentPane().add(panel1);
//        Конец включения элементов в тело самой формы
//        setPreferredSize(new Dimension(290,582));
        setPreferredSize(new Dimension(610,1050));
//        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        collectives.addActionListener(ev -> {
            System.out.println(Objects.requireNonNull(collectives.getSelectedItem()) + " Selected");
            String collNumList = Objects.requireNonNull(collectives.getSelectedItem()).toString();
            String shiftCode = Objects.requireNonNull(shifts.getSelectedItem()).toString();
            String operNumInput = text1.getText();
            //                -------------Create Connection to DB-------------
            mySQLAccess.mysqlOperations();
            try {
                if (mySQLAccess.isDBConnected(MySQLAccess.connection)) {
                    System.out.println("There is no Connection between java and mysql");
//                       ----------------- dialog box with error message -------------------------
                    JOptionPane.showMessageDialog(panel1,
                            "There is no Connection between java and mysql",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    //                -------------Creation Table in DB -------------
                    try {
                        mySQLAccess.createTable(MySQLAccess.connection, collNumList, shiftCode);
                    } catch (SQLException ex) {
                        System.out.println("Error while adding the record!");
                        throw new RuntimeException(ex);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            textArea.setText("");


            ArrayList listColls;
            try {
                listColls = ListOfTables.printTable(collNumList, shiftCode);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (Object item : listColls) {
                textArea.append(item +"\n");
            }
        });
        //Берем значение вводимое пользователем
        text1.addActionListener(e -> {
//                берем значение из инпута
            String collNumList = Objects.requireNonNull(collectives.getSelectedItem()).toString();
            String shiftCode = Objects.requireNonNull(shifts.getSelectedItem()).toString();
            String operNumInput = text1.getText();
            if(Objects.requireNonNull(collectives.getSelectedItem()).toString() =="---"){
                text1.setText("");
                JOptionPane.showMessageDialog(panel1,
                        "Please, choose the collective",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(operNumInput.equals("")){
                return;
            }
//                -------------Create Connection to DB-------------
            mySQLAccess.mysqlOperations();
            try {
                MySQLAccess.createDBTable(collNumList, clearNumber(operNumInput), shiftCode);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel1,
                        "The record "+clearNumber(operNumInput)+" already exists",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                text1.setText("");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            //Используем - Например вставляем в другое текстовое поле
//            textArea.append(MySQLAccess.getCurrentTime_db()+ " : " +shiftCode+" - "+collNumList+" - "+clearNumber(operNumInput)+"\n");
            text1.setText("");
            textArea.setText("");
            ArrayList listColls;
            try {
                listColls = ListOfTables.printTable(collNumList, shiftCode);
            } catch (SQLException eb) {
                throw new RuntimeException(eb);
            }

            for (Object item : listColls) {
                textArea.append(item +"\n");
            }
//                -------------Close Connection with DB -------------
            try {
                mySQLAccess.closeDBConnection();
            } catch (SQLException ex) {
                System.out.println("Error while connection!");
                throw new RuntimeException(ex);
            }
        });
        button.addActionListener(eve->{
            if(ListOfTables.tablesLength == 0){
                JOptionPane.showMessageDialog(panel1,
                        "Please, choose the collective",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                ListOfTables.executeTables();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JOptionPane.showMessageDialog(panel1,
                    "The File created and placed on Desktop \n \tWindow will close",
//                    "The File created and placed on Desktop",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }
    public static void main(String[] args) {
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(sSize);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(false);
                PresentAGui frame = null;
                try {
                    frame = new PresentAGui();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}