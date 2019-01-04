package priv.zzw.compiler;


import priv.zzw.compiler.tool.DFA;
import priv.zzw.compiler.tool.NFA;
import priv.zzw.compiler.tool.Symbol;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class TestCase {

    private enum Identity {
        VT, VN;
    }

    public static void main(String[] args) {
        TestCase testCase = new TestCase();

        testCase.testNFA();
    }

    public void testDFA() {
        DFA dfa;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (null == (dfa = DFA.loadFromFile("dfa.txt"))) {
            System.out.println("加载DFA失败");
        } else {
            dfa.displayDFA(new PrintStream(baos), "Ø");
            System.out.println(baos.toString());
            System.out.println("始态：" + dfa.getInitialState());
            List<String> list = new ArrayList<>();
            for (Integer i : dfa.getFinalStates()) {
                list.add(i.toString());
            }
            System.out.println("终态：" + String.join(",", list));
        }
    }

    public void testNFA() {
        NFA nfa;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ((nfa = NFA.loadNfaFromFile("nfa.txt")) != null) {
            nfa.printNFA(new PrintStream(baos), "Ø");
            System.out.println(baos.toString());
            //System.out.println(nfa.serializeNFA("nfa_copy.txt"));

            DFA dfa = nfa.determine();
            baos.reset();
            dfa.displayDFA(new PrintStream(baos), "Ø");
            System.out.println(baos.toString());
            System.out.println("始态：" + dfa.getInitialState());
            List<String> list = new ArrayList<>();
            for (Integer i : dfa.getFinalStates()) {
                list.add(i.toString());
            }
            System.out.println("终态：" + String.join(",", list));

            //calculate epsilon-closure
            Scanner s = new Scanner(System.in);
            PrintStream jout = System.out;
            String row;
            while (null != (row = s.nextLine())) {
                Set<Integer> states = new HashSet<>();
                for (String val : row.split(",")) {
                    states.add(Integer.parseInt(val));
                }
                row = s.nextLine();
                states = nfa.getIa(states, new Symbol(row, Symbol.V));
                String result = "{";
                for (Integer i : states) {
                    result += "" + i + ",";
                }
                result = result.substring(0, result.length() - 1) + "}";
                jout.println(result);
            }
        } else {
            System.out.println("加载NFA失败!");
        }
    }

}
