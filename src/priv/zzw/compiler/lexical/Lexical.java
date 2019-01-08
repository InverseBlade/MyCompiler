package priv.zzw.compiler.lexical;

import priv.zzw.compiler.grammar.Grammar;
import priv.zzw.compiler.grammar.Production;
import priv.zzw.compiler.tool.DFA;
import priv.zzw.compiler.tool.NFA;
import priv.zzw.compiler.tool.Symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lexical {

    /**
     * 右线性正规文法转换为DFA
     *
     * @param grammar 文法
     * @return DFA
     */
    public static NFA getNfaByGrammar(Grammar grammar) {
        //
        NFA nfa = new NFA();
        ArrayList<Symbol> stateList = new ArrayList<>();
        stateList.add(null);
        //状态命名
        for (Production production : grammar.getProductions()) {
            //左部非终结符
            Symbol left = production.getLeft();
            //不是非终结符，文法错误
            if (left.getType() != Symbol.VN) {
                return null;
            }
            if (!stateList.contains(left)) {
                stateList.add(left);
            }
        }
        //添加状态
        for (int i = 1; i < stateList.size(); i++) {
            nfa.getStates().add(i);
        }
        //添加初态
        nfa.getInitialStates().add(stateList.indexOf(grammar.getStart()));
        //添加终态
        int finalState = stateList.size();
        nfa.getStates().add(finalState);
        nfa.getFinalStates().add(finalState);
        //
        for (Production production : grammar.getProductions()) {
            int cur = stateList.indexOf(production.getLeft());
            Symbol rightFirst = production.getRight().get(0);
            int len = production.getRight().size();
            Set<Integer> target = new HashSet<>();

            nfa.getAlphabets().add(rightFirst);
            if (len < 2) {
                if (rightFirst.equals(nfa.EPSILON)) {
                    target.add(finalState);
                } else if (rightFirst.getType() == Symbol.VT) {
                    target.add(finalState);
                }
            } else {
                Symbol rightSecond = production.getRight().get(1);
                target.add(stateList.indexOf(rightSecond));
            }
            nfa.setF(cur, rightFirst, target);
        }
        return nfa;
    }

}






























