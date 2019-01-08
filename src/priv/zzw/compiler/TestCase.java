package priv.zzw.compiler;


import priv.zzw.compiler.grammar.Grammar;
import priv.zzw.compiler.grammar.Production;
import priv.zzw.compiler.lexical.Lexical;
import priv.zzw.compiler.tool.DFA;
import priv.zzw.compiler.tool.NFA;
import priv.zzw.compiler.tool.Symbol;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class TestCase {

    public static void main(String[] args) {
        //
        TestCase testCase = new TestCase();

        testCase.testGrammarToDFA();
    }

    public void testGrammarToDFA() {
        Grammar grammar = new Grammar();
        Scanner s = new Scanner(System.in);
        String row;
        boolean first = true;

        while (!(row = s.nextLine()).equals("end")) {
            String[] leftAndRight = row.split("->");
            Symbol left = new Symbol(leftAndRight[0], Symbol.VN);

            if (first) {
                grammar.setStart(left);
                first = false;
            }
            for (String item : leftAndRight[1].split("[|]")) {
                List<Symbol> right = new ArrayList<>();
                Symbol vt = new Symbol(item.substring(0, 1), Symbol.VT);
                right.add(vt);
                if (item.length() > 1) {
                    Symbol vn = new Symbol(item.substring(1, 2), Symbol.VN);
                    right.add(vn);
                }
                grammar.addProduction(new Production(left, right));
            }
        }
        NFA nfa = Lexical.getNfaByGrammar(grammar);
        nfa.printNFA(System.out, "-1");
        nfa.determine().displayDFA(System.out, "-1");

        DFA dfa = nfa.determine().minimize();
        dfa.displayDFA(System.out, "-1");
        System.out.println("初态：" + dfa.getInitialState());
        System.out.print("终态：");
        for (int i : dfa.getFinalStates()) {
            System.out.print(i + ",");
        }
    }

    public void testDfaMinimize() {
        DFA dfa = DFA.loadFromFile("nfa-to-dfa.txt");
        if (dfa != null) {
            dfa.displayDFA(System.out, "-1");

            DFA minDFA = dfa.minimize();
            minDFA.displayDFA(System.out, "-1");
            System.out.println("始态：" + minDFA.getInitialState());
            List<String> list = new ArrayList<>();
            for (Integer i : minDFA.getFinalStates()) {
                list.add(i.toString());
            }
            System.out.println("终态：" + String.join(",", list));
        }
    }

    public void testDFA() {
        NFA nfa = NFA.loadNfaFromFile("nfa.txt");
        DFA dfa;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (null == nfa) {
            System.out.println("加载DFA失败");
        } else {
            nfa.printNFA(System.out, "-1");
            dfa = nfa.determine();
            dfa.displayDFA(System.out, "-1");
            System.out.println(baos.toString());
            System.out.println("始态：" + dfa.getInitialState());
            List<String> list = new ArrayList<>();
            for (Integer i : dfa.getFinalStates()) {
                list.add(i.toString());
            }
            System.out.println("终态：" + String.join(",", list));
            System.out.println(dfa.serialize("nfa-to-dfa.txt"));
        }
    }

    public void testNFA() {
        NFA nfa;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ((nfa = NFA.loadNfaFromFile("nfa.txt")) != null) {
            nfa.printNFA(new PrintStream(baos), "Ø");
            System.out.println(baos.toString());
            //System.out.println(nfa.serializeNFA("nfa_copy.txt"));
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
