import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class PresentaGui extends JFrame {
    static JLabel lableLog = new JLabel();
    public PresentaGui() {
        super("Test frame");
        createGUI();
    }
    public void createGUI() {
        MySQLAccess mySQLAccess = new MySQLAccess();
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        начало позиционирования шапки
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        JLabel label1= new JLabel("Op. Number");
        JLabel label2= new JLabel("Col. Number");
        JLabel label3 = new JLabel("Shift");
        JTextField text1 = new JTextField();
        JComboBox collectives = new JComboBox();
        JComboBox shifts = new JComboBox();
        JTextArea textArea =new JTextArea();
        textArea.setEditable(false);
        JButton button = new JButton("Finish");
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
        lableLog.setBounds(10, 80,265,25);
        textArea.setBounds(5,120,265, 370);
        button.setBounds(65,500,155,40);
        panel1.add(label1);
        panel1.add(label2);
        panel1.add(text1);
        panel1.add(label3);
        panel1.add(lableLog);
        panel1.add(shifts);
        panel1.add(collectives);
        panel1.add(textArea);
        panel1.add(button);
//        Конец позиционирования шапки
//        начало включения элементов в тело самой формы
        getContentPane().add(panel1);
//        Конец включения элементов в тело самой формы
        setPreferredSize(new Dimension(285,580));
//        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        //Берем значение вводимое пользователем
        text1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                берем значение из инпута
                String collNumList = collectives.getSelectedItem().toString();
                String shiftCode = shifts.getSelectedItem().toString();
                String operNumInput = text1.getText();
                if(operNumInput.equals("")){
                    return;
                }
//                -------------Create Connection to DB-------------
                mySQLAccess.mysqlOperations();
//                -------------Creation Table in DB -------------
                try{
                    mySQLAccess.createDBTable(collNumList, operNumInput, shiftCode);
                }catch (SQLException | InterruptedException ex){
                    System.out.println("Error while adding the record!");
                    throw new RuntimeException(ex);
                }
//                -------------Close Connection with DB -------------
                try {
                    mySQLAccess.closeDBConnection();
                } catch (SQLException ex) {
                    System.out.println("Error while connection!");
                    throw new RuntimeException(ex);
                }
                //Используем - Например вставляем в другое текстовое поле
                textArea.append(MySQLAccess.currentTime_db+ " : " +shiftCode+" - "+collNumList+" - "+operNumInput+"\n");
                text1.setText("");
            }
        });
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                PresentaGui frame = new PresentaGui();
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}