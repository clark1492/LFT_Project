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
	public void start() {
        if(look.tag=='('|| look.tag==Tag.NUM){
            expr();
            match(Tag.EOF);
        }else
            error("system error in start");
	}
	private void expr() {
        if(look.tag=='('|| look.tag==Tag.NUM){
            term();
            exprp();
        }else
            error("system error in expr");
	}
	private void exprp() {
        if(look.tag=='+' || look.tag=='-' || look.tag==Tag.EOF || look.tag==')'){
            switch (look.tag) {
                case '+':
                    move();
                    term();
                    exprp();
                    break;
                case '-':
                    move();
                    term();
                    exprp();
                    break;
            }
        }else
            error("system error in exprp");
	}
	private void term() {
        if(look.tag=='('|| look.tag==Tag.NUM){
            fact();
            termp();
        }else
            error("system error in term");
	}
	private void termp() {
        if(look.tag=='*' || look.tag=='/' || look.tag=='+' || look.tag=='-' || look.tag==Tag.EOF || look.tag==')'){
            switch (look.tag) {
                case '*':
                    move();
                    fact();
                    termp();
                    break;
                case '/':
                    move();
                    fact();
                    termp();
                    break;
            }
        }else
            error("syntax error in termp");
	}
	private void fact() {
        if(look.tag=='(' || look.tag==Tag.NUM){
            switch (look.tag) {
                case '(':
                    match('(');
                    expr();
                    switch(look.tag){
                        case ')':
                            move();
                            break;
                    }
                    break;
                case Tag.NUM:
                    match(Tag.NUM);
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
			parser.start();
			System.out.println("Input OK");
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}