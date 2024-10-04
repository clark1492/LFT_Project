import java.io.*;
public class Parser {
   
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
   
    public Parser(Lexer l, BufferedReader br) {
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
        if(look.tag==Tag.BEGIN || look.tag==Tag.FOR || look.tag==Tag.IF || look.tag==Tag.READ || look.tag==Tag.PRINT || look.tag==Tag.ID){
            statlist();
            match(Tag.EOF);
        } else
            error("syntax error in prog");
    }
    private void stat() {
        if(look.tag==Tag.ID || look.tag==Tag.BEGIN || look.tag==Tag.PRINT || look.tag==Tag.READ || look.tag==Tag.IF || look.tag==Tag.FOR){
            switch(look.tag){
                case Tag.ID:
                    match(Tag.ID);
                    match('=');
                    expr();
                    break;
                case Tag.PRINT:
                    match(Tag.PRINT);
                    match('(');
                    expr();
                    match(')');
                    break;
                case Tag.READ:
                    match(Tag.READ);
                    match('(');
                    match(Tag.ID);
                    match(')');
                    break;
                case Tag.IF:
                    match(Tag.IF);
                    bexpr();
                    match(Tag.THEN);
                    stat(); 
                        if(look.tag==Tag.ELSE){
                            match(Tag.ELSE);
                            stat();
                        }
                    break;
                case Tag.FOR:
                    match(Tag.FOR);
                    match('(');
                    match(Tag.ID);
                    match('=');
                    expr();
                    match(';');
                    bexpr();
                    match(')');
                    match(Tag.DO);
                    stat();
                    break;
                case Tag.BEGIN:
                    match(Tag.BEGIN);
                    statlist();
                    match(Tag.END);
                    break;
            }
        }else
            error("syntax error in stat");
    }
    private void statlist() {
        if(look.tag==Tag.BEGIN || look.tag==Tag.FOR || look.tag==Tag.IF || look.tag==Tag.READ || look.tag==Tag.PRINT || look.tag==Tag.ID){
            stat();
            statlistp();
        } else
            error("syntax error in statlist");
    }
    private void statlistp() {
        if(look.tag==';' || look.tag==Tag.EOF || look.tag==Tag.END){
            switch(look.tag){
                case ';':
                    match(';');
                    stat();
                    statlistp();
                    break;
            }
        }else
            error("syntax error in statlistp");
    }
    private void bexpr() {
        if(look.tag==Tag.NUM || look.tag==Tag.ID || look.tag=='('){
            expr();
            match(Tag.RELOP);
            expr();
        } else
            error("syntax error in bexpr");
    }
    private void expr() {
        if(look.tag==Tag.NUM || look.tag==Tag.ID || look.tag=='('){
            term();
            exprp();
        } else
            error("syntax error in expr");
    }
    private void exprp() {
        if(look.tag=='+' || look.tag=='-' || look.tag==Tag.RELOP || look.tag==')' || look.tag==Tag.THEN || look.tag==Tag.ELSE || look.tag==';' || look.tag==Tag.EOF || look.tag==Tag.END){
            switch (look.tag) {
                case '+':
                    match('+');
                    term();
                    exprp();
                    break;
                case '-':
                    match('-');
                    term();
                    exprp();
                    break;
            }
        }else
            error("syntax error in exprp");
    }
    private void term() {
        if(look.tag==Tag.NUM || look.tag==Tag.ID || look.tag=='('){
            fact();
            termp();
        }else
            error("syntax error in term");
    }
    private void termp() {
        if(look.tag=='*' || look.tag=='/' || look.tag=='+' || look.tag=='-' || look.tag==Tag.RELOP || look.tag==')' || look.tag==Tag.THEN || look.tag==Tag.ELSE || look.tag==';' || look.tag==Tag.EOF || look.tag==Tag.END){
            switch (look.tag) {
                case '*':
                    match('*');
                    fact();
                    termp();
                    break;
                case '/':
                    match('/');
                    fact();
                    termp();
                    break;
            }
        }else
            error("syntax error in termp");
    }
    private void fact() {
        if(look.tag=='(' || look.tag==Tag.ID || look.tag==Tag.NUM){
            switch (look.tag) {
                case '(':
                    match('(');
                    expr();
                    System.out.println(look.tag);
                    match(')');
                    break;
                case Tag.NUM:
                    match(Tag.NUM);
                    break;
                case Tag.ID:
                    match(Tag.ID);
                    break;
            }
        }else
            error("syntax error in fact");
        }
   
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Prova.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}