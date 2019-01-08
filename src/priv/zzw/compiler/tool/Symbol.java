package priv.zzw.compiler.tool;

import java.util.Arrays;

public class Symbol implements Comparable<Symbol> {

    public final static int VN = 0;

    public final static int VT = 1;

    public final static int V = 2;

    private String symbol;

    private int type;

    private final static Integer[] types = new Integer[]{VN, VT, V};

    public Symbol(String symbol, int type) {
        this.symbol = symbol;
        this.type = Arrays.asList(types)
                .contains(type) ? type : VT;
    }

    public boolean equals(Symbol symbol) {
        return type == symbol.type
                && this.symbol.equals(symbol.symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            return type == ((Symbol) obj).type
                    && symbol.equals(((Symbol) obj).symbol);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Symbol o) {
        return symbol.compareTo(o.symbol);
    }
}
