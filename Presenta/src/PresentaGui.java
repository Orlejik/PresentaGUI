import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;
public class PresentAGui extends JFrame {
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
    public PresentAGui() {
        super("Presenta");
        createGUI();
    }
    public void createGUI() {
        MySQLAccess mySQLAccess = new MySQLAccess();
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        начало позиционирования шапки
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        JLabel label1= new JLabel("Op. Number", SwingConstants.CENTER);
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
        collectives.addItem("18001");
        collectives.addItem("18002");
        collectives.addItem("18003");
        collectives.addItem("18004");
        collectives.addItem("18005");
        collectives.addItem("18006");
        collectives.addItem("18007");
        collectives.addItem("18008");
        collectives.addItem("18009");
        shifts.addItem("M");
        shifts.addItem("T");
        shifts.addItem("N");
        label1.setBounds(5,5,90,25);
        label2.setBounds(180, 5, 90,25);
        label3.setBounds(107, 5, 60, 25);
        text1.setBounds(5,45,90,25);
        collectives.setBounds(180,45,90,25);
        shifts.setBounds(107, 45, 60, 25);
        lableLog.setBounds(10, 525,265,25);
        textArea.setBounds(5,85,265, 385);
//        textArea.setLocation(5, 85);
//        textArea.setPreferredSize(sSize);
        button.setBounds(65,490,155,30);
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
        lableLog.setFont(new Font("Sans Serif", Font.PLAIN, 8));
        lableLog.setForeground(Color.gray);
//        Конец позиционирования шапки
//        начало включения элементов в тело самой формы
        getContentPane().add(panel1);
//        Конец включения элементов в тело самой формы
        setPreferredSize(new Dimension(290,582));
//        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        collectives.addActionListener(ev -> {
            textArea.setText("");
            System.out.println(Objects.requireNonNull(collectives.getSelectedItem()) + " Selected");
            String collNumList = Objects.requireNonNull(collectives.getSelectedItem()).toString();
            String shiftCode = Objects.requireNonNull(shifts.getSelectedItem()).toString();
            String operNumInput = text1.getText();
            //                -------------Create Connection to DB-------------
            mySQLAccess.mysqlOperations();
            try {
                if(mySQLAccess.isDBConnected(MySQLAccess.connection)){
                    System.out.println("There is no Connection between java and mysql");
//                       ----------------- dialog box with error message -------------------------
                    JOptionPane.showMessageDialog(panel1,
                            "There is no Connection between java and mysql",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }else{
                    //                -------------Creation Table in DB -------------
                    try{
                        mySQLAccess.createTable(MySQLAccess.connection, collNumList, shiftCode);
                    }catch (SQLException ex){
                        System.out.println("Error while adding the record!");
                        throw new RuntimeException(ex);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            //Используем - Например вставляем в другое текстовое поле
            textArea.append(MySQLAccess.getCurrentTime_db()+ " : " +shiftCode+" - "+collNumList+" - "+clearNumber(operNumInput)+"\n");
            text1.setText("");
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
        });
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(false);
                PresentAGui frame = new PresentAGui();
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}