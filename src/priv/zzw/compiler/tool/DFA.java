package priv.zzw.compiler.tool;

import java.io.PrintStream;
import java.util.*;

public class DFA {

    private NFA nfa = new NFA();

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
        nfa.setF(current, new Symbol(alpha, Symbol.VT), set);
    }

    public Integer f(Integer current, String alpha) {
        Set<Integer> target;
        if (null == (target = nfa.f(current, new Symbol(alpha, Symbol.VT)))) {
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

    /**
     * DFA化简
     *
     * @return DFA
     */
    public DFA minimize() {
        DFA dfa = new DFA();
        Set<Integer> a = new HashSet<>(getStates());
        Set<Integer> b = new HashSet<>(getFinalStates());
        ArrayList<Set<Integer>> dist = new ArrayList<>();
        boolean sign = true;
        //a为非终态
        a.removeAll(b);
        dist.add(a);
        dist.add(b);
        //划分等价状态
        while (sign) {
            //dist不再改变就退出
            sign = false;
            ArrayList<Set<Integer>> nextDist = new ArrayList<>();
            //
            for (Set<Integer> equiv : dist) {
                //对上一次每一个等价划分继续划分
                int tar0 = equiv.iterator().next();
                Set<Integer> temp = new HashSet<>();
                //试探每个字符
                for (Symbol alpha : getAlphabets()) {
                    //处理一个划分中每个状态
                    for (int state : equiv) {
                        if (state == tar0)
                            continue;
                        //对一个等价划分中所有状态进行判断
                        Integer s1 = f(state, alpha.getSymbol());
                        Integer s2 = f(tar0, alpha.getSymbol());
                        boolean flag = false;
                        //判断s1,s2两个状态对同一字符转换后的目标状态是否满足上一次等价
                        for (Set<Integer> set : dist) {
                            if (set.contains(s1) && set.contains(s2)) {
                                flag = true;
                                break;
                            }
                        }
                        //两个状态可区分
                        if (!flag) {
                            temp.add(state);
                        }
                    }
                }
                if (!temp.isEmpty()) {
                    //若当前划分可以继续划分
                    nextDist.add(temp);
                    equiv.removeAll(temp);
                    nextDist.add(equiv);
                    sign = true;
                } else {
                    //无法继续划分
                    nextDist.add(equiv);
                }
            }
            //进入下一次迭代
            dist = nextDist;
        }
        dist.sort(new Comparator<Set<Integer>>() {
            @Override
            public int compare(Set<Integer> o1, Set<Integer> o2) {
                boolean flag = false;
                for (int state : o1) {
                    if (getFinalStates().contains(state)) {
                        flag = true;
                        break;
                    }
                }
                if (flag || o2.contains(getInitialState())) {
                    return 1;
                } else if (!o2.contains(getInitialState())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        //添加状态
        for (int i = 1; i <= dist.size(); i++) {
            dfa.getStates().add(i);
        }
        //设置符号表
        dfa.getAlphabets().addAll(getAlphabets());
        //化简dfa
        for (int i = 0; i < dist.size(); i++) {
            for (Symbol alpha : dfa.getAlphabets()) {
                for (int state : dist.get(i)) {
                    Integer tar = f(state, alpha.getSymbol());
                    for (int j = 0; j < dist.size(); j++) {
                        if (dist.get(j).contains(tar)) {
                            tar = j + 1;
                            break;
                        }
                    }
                    dfa.setF(i + 1, alpha.getSymbol(), tar);
                    //设置初态
                    if (getInitialState().equals(state)) {
                        dfa.getNfa().getInitialStates().add(i + 1);
                    }
                    //设置终态
                    if (getFinalStates().contains(state)) {
                        dfa.getFinalStates().add(i + 1);
                    }
                }
            }
        }
        System.out.println("终态总数：" + dfa.getNfa().getFinalStates().size());
        for (Set<Integer> set : dist) {
            System.out.print("{");
            for (int s : set) {
                System.out.print(s + ",");
            }
            System.out.print("},");
        }
        System.out.println("");
        return dfa;
    }

}


































