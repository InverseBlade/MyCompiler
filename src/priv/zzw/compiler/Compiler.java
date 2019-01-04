package priv.zzw.compiler;

public class Compiler {

    private String source;

    public Compiler(String source) {
        this.source = source;
    }

    public void compile() {
        System.out.println("Compiled Successfully!");
    }

}
