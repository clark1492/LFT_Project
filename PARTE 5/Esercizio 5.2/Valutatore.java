import java.io.*;

public class Valutatore {
	
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;
	
	public Valutatore(Lexer l, BufferedReader br) {
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
        if(look.tag=='(' || look.tag==Tag.NUM){
            int expr_val;
            expr_val = expr();
            match(Tag.EOF);
            System.out.println(expr_val);
        }else
            error("system error in start");
	}
	private int expr() {
        int term_val, exprp_val=0;
        if(look.tag=='(' || look.tag==Tag.NUM){
            term_val = term();
            exprp_val=exprp(term_val);
        }else
            error("system error in expr");
        return exprp_val;
	}

	private int exprp(int exprp_i) {
		int term_val, exprp_val=0;
        if(look.tag=='+' || look.tag=='-' || look.tag==Tag.EOF || look.tag==')'){
            switch (look.tag) {
                case '+':
                    match('+');
                    term_val = term();
                    exprp_val = exprp(exprp_i + term_val);
                    break;
                case '-':
                    match('-');
                    term_val=term();
                    exprp_val = exprp(exprp_i - term_val);
                    break;
                default:
                    exprp_val=exprp_i;
            }
        }else
            error("syntax error in exprp");
        return exprp_val;
	}
	private int term() {
		int fact_val, termp_val=0;
        if(look.tag=='(' || look.tag==Tag.NUM){
            fact_val=fact();
            termp_val=termp(fact_val);
        }else
            error("syntax error in term");
		return termp_val;
	}
	private int termp(int termp_i) {
		int termp_val=0, fact_val;
        if(look.tag=='*' || look.tag=='/' || look.tag=='+' || look.tag=='-' || look.tag==')' || look.tag==Tag.EOF){
            switch (look.tag) {
                case '*':
                    match('*');
                    fact_val=fact();
                    termp_val=termp(termp_i * fact_val);
                    break;
                case '/':
                    match('/');
                    fact_val=fact();
                    termp_val=termp(termp_i / fact_val);
                    break;
                default:
                    termp_val=termp_i;
            }
        }else
            error("syntax error in termp");
		return termp_val;
	}
	private int fact() {
		int fact_val=0;
        if(look.tag=='(' || look.tag==Tag.NUM){
            switch (look.tag) {
                case '(':
                    match('(');
                    fact_val=expr();
                    switch(look.tag){
                        case ')':
                            match(')');
                            break;
                        default:
                            error("syntax error");
                    }
                    break;
                case Tag.NUM:
                    fact_val= ((NumberTok)look).lexeme;
                    match(Tag.NUM);
                    break;
                default:
                    error("syntax error");
                    break;
            }
        }else
            error("syntax error in fact");
		return fact_val;
	}
	
	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Prova.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Valutatore valutatore = new Valutatore(lex, br);
			valutatore.start();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}