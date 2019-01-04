package priv.zzw.compiler.lexical;

public class Token {

    //标识符
    public static final int IDENTIFIER = 0;

    //操作数
    public static final int OPERAND = 1;

    //运算符
    public static final int OPERATOR = 2;

    //界符
    public static final int DELIMITER = 3;

    //Token
    private String token;

    //种别码
    private int code;

    public Token(String token, int code) {
        this.token = token;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
