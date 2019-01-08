package priv.zzw.compiler.grammar;

import priv.zzw.compiler.tool.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private Symbol start;

    private List<Production> productions = new ArrayList<>();

    public Symbol getStart() {
        return start;
    }

    public void setStart(Symbol start) {
        this.start = start;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

}
