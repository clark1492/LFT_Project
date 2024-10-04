import java.io.*;
 
public class ExpressionTranslator {
   
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
   
    CodeGenerator code = new CodeGenerator();
 
    public ExpressionTranslator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look); 
    }
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }
    public void prog() {
        if(look.tag==Tag.PRINT){
            match(Tag.PRINT);
            match('(');
            expr();
            code.emit(OpCode.invokestatic,1);
            match(')');
            match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            };
        }else
            error("syntax error in prog");
    }
    private void expr(){
        if(look.tag=='(' || look.tag==Tag.NUM){
            term();
            exprp();
        }else
            error("syntax error in expr");
    }
    private void exprp() {
        if(look.tag=='+' || look.tag=='-' || look.tag==')' || look.tag==Tag.EOF){
            switch(look.tag) {
                case '+':
                    match('+');
                    term();
                    code.emit(OpCode.iadd);
                    exprp();
                    break;
                case '-':
                    match('-');
                    term();
                    code.emit(OpCode.isub);
                    exprp();
                    break;
            }
        }else
            error("syntax error in exprp");
    }
    private void term(){
        if(look.tag==Tag.NUM || look.tag=='('){
            fact();
            termp();
        }else
            error("syntax error in term");
    }
    private void termp() {
        if(look.tag=='*' || look.tag=='/' || look.tag=='+' || look.tag=='-' || look.tag==')' || look.tag==Tag.EOF){
            switch(look.tag) {
                case '*':
                    match('*');
                    fact();
                    code.emit(OpCode.imul);
                    termp();
                break;
                case '/':
                    match('/');
                    term();
                    code.emit(OpCode.idiv);
                    termp();
                break;
            }
        }else
            error("syntax error in termp");
    }
    private void fact(){
        if(look.tag==Tag.NUM || look.tag=='('){
            switch(look.tag) {
                case '(':
                    match('(');
                    expr();
                    match(')');
                    break;
                case Tag.NUM:
                    code.emit(OpCode.ldc,((NumberTok)look).lexeme);
                    match(Tag.NUM);
            }
        }else
            error("syntax error in fact");
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Prova.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ExpressionTranslator expressiontranslator = new ExpressionTranslator(lex, br);
            expressiontranslator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}