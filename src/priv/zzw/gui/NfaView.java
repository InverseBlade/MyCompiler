package priv.zzw.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import priv.zzw.compiler.tool.NFA;

public class NfaView {
    private JPanel rootPanel;
    private JPanel Header;
    private JEditorPane editorPane1;
    private JTextField editorISet;
    private JButton getClosureButton;
    private JEditorPane editorClosure;
    private JPanel Footer;
    private JButton comfirmButton;

    public static JFrame frame;

    //NFA
    private NFA nfa = null;

    private NfaView() {
        comfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nfa = NFA.loadNfaFromFile("nfa.txt");
                if (nfa == null) {
                    editorPane1.setText("ERROR");
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                nfa.printNFA(new PrintStream(baos), "1");
                editorPane1.setText(baos.toString());
            }
        });
        getClosureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nfa == null || editorISet.getText().isEmpty()) {
                    return;
                }
                Set<Integer> states = new HashSet<>();
                for (String s : editorISet.getText().split(",")) {
                    states.add(Integer.parseInt(s));
                }
                Set<String> closure = new HashSet<>();
                for (Integer s : nfa.getClosure(states)) {
                    closure.add(s.toString());
                }
                String result = "{" + String.join(",", closure) + "}";
                editorClosure.setText(result);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("NFAView");
        frame.setContentPane(new NfaView().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        NfaView.frame = frame;
        //设置界面风格
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }

}
