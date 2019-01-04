package priv.zzw.compiler.grammar;

import priv.zzw.compiler.tool.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Production {

    private Symbol left;

    private List<Symbol> right = new ArrayList<>();

    public Production(Symbol left) {
        this.left = left;
    }

    public Production(String left) {
        this.left = new Symbol(left, Symbol.VN);
    }

    public Production(Symbol left, List<Symbol> right) {
        this.left = left;
        this.right = right;
    }

    public Symbol getLeft() {
        return left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public void setLeft(Symbol left) {
        this.left = left;
    }

    public void setRight(List<Symbol> right) {
        this.right = right;
    }
}
