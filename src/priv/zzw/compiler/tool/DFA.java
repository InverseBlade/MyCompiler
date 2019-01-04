package priv.zzw.compiler.tool;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class DFA {

    private NFA nfa = new NFA(new Symbol("ε", Symbol.V));

    DFA() {

    }

    DFA(Symbol epsilon) {
        nfa = new NFA(epsilon);
    }

    private DFA(NFA nfa) {
        this.nfa = nfa;
    }

    NFA getNfa() {
        return nfa;
    }

    public void setF(Integer current, String alpha, Integer target) {
        HashSet<Integer> set = new HashSet<>();
        set.add(target);
        nfa.setF(current, new Symbol(alpha, Symbol.V), set);
    }

    public Integer f(Integer current, String alpha) {
        Set<Integer> target;
        if (null == (target = nfa.f(current, new Symbol(alpha, Symbol.V)))) {
            return null;
        }
        return target.iterator().next();
    }

    public Set<Integer> getStates() {
        return nfa.getStates();
    }

    public Set<Symbol> getAlphabets() {
        return nfa.getAlphabets();
    }

    public Integer getInitialState() {
        return nfa.getInitialStates().iterator().next();
    }

    public Set<Integer> getFinalStates() {
        return nfa.getFinalStates();
    }

    public void displayDFA(PrintStream jout, String emptySymbol) {
        nfa.printNFA(jout, emptySymbol);
    }

    public String serialize(String path) {
        return nfa.serializeNFA(path);
    }

    public static DFA loadFromFile(String path) {
        NFA nfa = NFA.loadNfaFromFile(path);
        if (nfa != null) {
            return new DFA(nfa);
        } else {
            return null;
        }
    }

    public DFA minimize() {
        return this;
    }

}
