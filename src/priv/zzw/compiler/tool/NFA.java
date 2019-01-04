package priv.zzw.compiler.tool;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class NFA {

    public final Symbol EPSILON;

    //状态集
    private Set<Integer> states = new HashSet<>();

    //字母表
    private Set<Symbol> alphabets = new HashSet<>();

    //状态转换矩阵
    private Map<Integer, Map<Symbol, Set<Integer>>> mRow = new HashMap<>();

    //初态集
    private Set<Integer> initialStates = new HashSet<>();

    //终态集
    private Set<Integer> finalStates = new HashSet<>();

    NFA(Symbol epsilon) {
        this.EPSILON = epsilon;
    }

    public Set<Integer> getStates() {
        return states;
    }

    public Set<Symbol> getAlphabets() {
        return alphabets;
    }

    public Set<Integer> getInitialStates() {
        return initialStates;
    }

    public Set<Integer> getFinalStates() {
        return finalStates;
    }

    //按映射获得一个状态集
    public Set<Integer> f(Integer state, Symbol alphabet) {
        if (mRow.containsKey(state)
                && mRow.get(state).containsKey(alphabet)) {
            return mRow.get(state).get(alphabet);
        }
        return null;
    }

    //设置一个映射
    public void setF(Integer state, Symbol alphabet, Set<Integer> targets) {
        if (!states.contains(state)
                || !alphabets.contains(alphabet)
                || !states.containsAll(targets)) {
            return;
        }
        if (mRow.containsKey(state)) {
            mRow.get(state).put(alphabet, targets);
        } else {
            Map<Symbol, Set<Integer>> mCol = new HashMap<>();
            mCol.put(alphabet, targets);
            mRow.put(state, mCol);
        }
    }

    //为目标映射添加目标状态
    public void addTargetToF(Integer state, Symbol alphabet, Set<Integer> targets) {
        Set<Integer> tar = f(state, alphabet);
        if (tar == null) {
            setF(state, alphabet, targets);
        } else {
            tar.addAll(targets);
            setF(state, alphabet, tar);
        }
    }

    /**
     * load NFA from file
     *
     * @param path File Path
     * @return NFA or null where failed
     */
    public static NFA loadNfaFromFile(String path) {
        try {
            File file = new File(path);
            BufferedReader bfrd =
                    new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            NFA nfa;
            String row;
            ArrayList<Symbol> symList = new ArrayList<>();
            //读ε
            String epsilon = String.valueOf((char) Integer.parseInt(bfrd.readLine()));
            nfa = new NFA(new Symbol(epsilon, Symbol.V));
            //读字符表
            row = bfrd.readLine();
            for (String val : row.split(",")) {
                String symbolName = String.valueOf((char) Integer.parseInt(val));
                Symbol symbol = new Symbol(symbolName, Symbol.V);
                if (symList.contains(symbol)) {
                    return null;
                }
                symList.add(symbol);
                nfa.alphabets.addAll(symList);
            }
            //读状态集
            int num = Integer.parseInt(bfrd.readLine());
            for (int i = 1; i <= num; i++) {
                nfa.states.add(i);
            }
            //读起始状态集
            row = bfrd.readLine();
            for (String val : row.split(",")) {
                int state = Integer.parseInt(val);
                nfa.initialStates.add(state);
            }
            //读终态集
            row = bfrd.readLine();
            for (String val : row.split(",")) {
                int state = Integer.parseInt(val);
                nfa.finalStates.add(state);
            }
            //读映射规则
            for (int i = 1; i <= nfa.states.size(); i++) {
                String[] cols = bfrd.readLine().split(",");
                for (int j = 0; j < symList.size(); j++) {
                    Set<Integer> list = new HashSet<>();
                    for (String s : cols[j].split("[|]")) {
                        list.add(Integer.parseInt(s));
                    }
                    nfa.addTargetToF(i, symList.get(j), list);
                }
            }
            bfrd.close();
            return nfa;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 美化输出转换矩阵图
     *
     * @param jout        输出目标
     * @param emptySymbol 空集标识
     */
    public void printNFA(PrintStream jout, String emptySymbol) {
        //
        ArrayList<Symbol> alphabets = new ArrayList<>(getAlphabets());
        List<Integer> states = new ArrayList<>(getStates());

        Collections.sort(alphabets);
        int len = 1;
        for (Integer state : states) {
            for (Symbol symbol : alphabets) {
                Set<Integer> tar = f(state, symbol);
                if (tar == null)
                    continue;
                int l;
                if ((l = tar.size()) > len)
                    len = l;
            }
        }
        len = 2 * len + 1;
        //Print
        String outputFormat = "|%-" + len + "s";
        jout.print(String.format(outputFormat, " "));
        for (Symbol a : alphabets) {
            jout.print(String.format(outputFormat, a.getSymbol()));
        }
        jout.print("\r\n");
        for (Integer state : states) {
            jout.print(String.format(outputFormat, state));
            for (Symbol a : alphabets) {
                Set<Integer> targets = f(state, a);
                String result;
                if (targets != null) {
                    Set<String> str = new HashSet<>();
                    targets.forEach(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) {
                            str.add(integer.toString());
                        }
                    });
                    result = String.join(",", str);
                } else {
                    result = emptySymbol;
                }
                jout.print(String.format(outputFormat, result));
            }
            jout.print("\r\n");
        }
    }

    /**
     * serialize NFA
     *
     * @param path file path
     * @return serialization of NFA
     */
    public String serializeNFA(String path) {
        //
        StringBuilder res = new StringBuilder();
        ArrayList<Symbol> symList = new ArrayList<>(alphabets);
        String enter = "\r\n";
        //输出Epsilon的unicode
        res.append(String.valueOf((int) EPSILON.getSymbol().charAt(0)));
        res.append(enter);
        //输出字符表
        List<String> sym = new ArrayList<>();
        Collections.sort(symList);
        symList.forEach(new Consumer<Symbol>() {
            @Override
            public void accept(Symbol symbol) {
                sym.add(String.valueOf((int) symbol.getSymbol().charAt(0)));
            }
        });
        res.append(String.join(",", sym));
        res.append(enter);
        //输出状态个数
        res.append(Collections.max(states));
        res.append(enter);
        //输出始态
        List<String> stateList = new ArrayList<>();
        for (Integer state : initialStates) {
            stateList.add(state.toString());
        }
        res.append(String.join(",", stateList));
        res.append(enter);
        //输出终态
        stateList.clear();
        for (Integer state : finalStates) {
            stateList.add(state.toString());
        }
        res.append(String.join(",", stateList));
        res.append(enter);
        //输出转换矩阵
        for (Integer state : states) {
            List<String> row = new ArrayList<>();
            for (Symbol symbol : symList) {
                Set<Integer> tar = f(state, symbol);
                if (tar == null) {
                    row.add("-1");
                } else {
                    List<String> cell = new ArrayList<>();
                    for (Integer s : tar) {
                        cell.add(s.toString());
                    }
                    row.add(String.join("|", cell));
                }
            }
            res.append(String.join(",", row));
            res.append(enter);
        }
        //Save File
        try {
            if (path != null && !path.equals("")) {
                BufferedWriter bw =
                        new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(path))
                        );
                bw.write(res.toString());
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    /**
     * 计算e-closure闭包
     *
     * @param states I
     * @return Closure
     */
    public Set<Integer> getClosure(Set<Integer> states) {
        Set<Integer> closure = new HashSet<>(states);
        for (Integer state : states) {
            //DFS
            Stack<Integer> stack = new Stack<>();
            Set<Integer> temp = f(state, EPSILON);
            if (temp != null) {
                stack.addAll(temp);
            }
            while (!stack.empty()) {
                Integer top = stack.pop();
                closure.add(top);
                if ((temp = f(top, EPSILON)) != null) {
                    stack.addAll(temp);
                }
            }
        }
        return closure;
    }

    /**
     * 计算Ia
     *
     * @param states 状态子集
     * @param alpha  字符
     * @return Ia
     */
    public Set<Integer> getIa(Set<Integer> states, Symbol alpha) {
        Set<Integer> j = new HashSet<>();
        for (Integer s : states) {
            Set<Integer> tar;
            if (null != (tar = f(s, alpha))) {
                j.addAll(tar);
            }
        }
        return getClosure(j);
    }

    /**
     * NFA确定化为DFA
     *
     * @return DFA
     */
    public DFA determine() {
        //
        DFA dfa = new DFA();
        int stateCount = 1;
        ArrayList<Set<Integer>> subset = new ArrayList<>();

        subset.add(null);
        subset.add(1, getClosure(initialStates));
        dfa.getNfa().initialStates.add(1);
        dfa.getStates().add(1);
        dfa.getAlphabets().addAll(alphabets);
        dfa.getAlphabets().remove(EPSILON);

        for (int i = 1; i <= stateCount; i++) {
            for (Symbol alpha : dfa.getAlphabets()) {
                Set<Integer> tar;
                if (1 > (tar = getIa(subset.get(i), alpha)).size()) {
                    continue;
                }
                if (!subset.contains(tar)) {
                    subset.add(++stateCount, tar);
                    dfa.getNfa().states.add(stateCount);
                    //标记终态
                    for (Integer f : finalStates) {
                        if (tar.contains(f)) {
                            dfa.getFinalStates().add(stateCount);
                            break;
                        }
                    }
                }
                dfa.setF(i, alpha.getSymbol(), subset.indexOf(tar));
            }
        }
        return dfa;
    }

}
































