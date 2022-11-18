package experiment3;

import experiment2.LL_1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author 李天翔
 * @date 2022/05/20
 **/
public class MyFrame extends JFrame {
    static JButton btnLR_1 = new JButton("LR(1)分析");
    static JPanel MyPanel = new JPanel();
    static Vector<String> column = new Vector<String>();
    static Vector<String> row = new Vector<>();
    static JTable table;
    static JTextArea area = new JTextArea();

    public static void main(String[] args) {

        new MyFrame("LR(1)分析界面");
    }

    MyFrame(String title) {
        super(title);
        setSize(800, 700);
        setResizable(false);
        MyPanel.setLayout(null);

        column.add("步骤");
        column.add("状态栈");
        column.add("符号栈");
        column.add("输入串");
        column.add("动作说明");

        table = new JTable(row, column);
        table.getColumn(column.get(4)).setPreferredWidth(200);
        area.setFont(new Font("宋体", Font.PLAIN, 25));
        //area.setText("i+i*i#");
        JScrollPane scrollPanel1 = new JScrollPane(table);
        JScrollPane scrollPanel2 = new JScrollPane(area);
        MyPanel.add(btnLR_1);
        MyPanel.add(scrollPanel1);
        MyPanel.add(scrollPanel2);
        btnLR_1.setBounds(600, 160, 150, 30);
        scrollPanel1.setBounds(20, 200, 740, 450);
        scrollPanel2.setBounds(20, 5, 740, 150);
        btnLR_1.addActionListener(new Listener());
        this.add(MyPanel);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class Listener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == MyFrame.btnLR_1) {
            LR_1.input = MyFrame.area.getText();
            LR_1.analyze();
            for (int i = 0; i < LR_1.stepsList.size(); i++) {
                Object[] o = new Object[5];
                o[0] = LR_1.stepsList.get(i).numStep;
                o[1] = LR_1.stepsList.get(i).stateStack;
                o[2] = LR_1.stepsList.get(i).characterStack;
                o[3] = LR_1.stepsList.get(i).inString;
                o[4] = LR_1.stepsList.get(i).productString;
                ((DefaultTableModel) MyFrame.table.getModel()).addRow(o);
            }
        }
    }
}
