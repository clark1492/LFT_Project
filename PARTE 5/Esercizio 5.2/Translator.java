import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
	    if (look.tag != Tag.EOF) move();
	} else error("Syntax error");
    }

    public void prog() {        
	if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN) {
        int lnext_prog = code.newLabel();
       	statlist(lnext_prog);
		code.emitLabel(lnext_prog);
		match(Tag.EOF);
        try {
            code.toJasmin();
        }
        catch(java.io.IOException e) {
            System.out.println("IO error\n");
        };
	} else
		error("syntax error in prog");
    }

    public void stat(int lnext) {
        if(look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN){
            switch (look.tag) {
                case Tag.ID:
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr == -1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match('=');
                    expr();
                    code.emit(OpCode.istore, id_addr);
                    break;
                case Tag.PRINT:
                    match(Tag.PRINT);
                    match('(');
                    expr();
                    match(')');
                    code.emit(OpCode.invokestatic,1);
                    break;
                case Tag.READ:
                    match(Tag.READ);
                    match('(');
                    if (look.tag==Tag.ID) {
                        int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                        if (read_id_addr == -1) {
                            read_id_addr = count;
                            st.insert(((Word)look).lexeme,count++);
                        }                    
                        match(Tag.ID);
                        match(')');
                        code.emit(OpCode.invokestatic,0);
                        code.emit(OpCode.istore,read_id_addr);   
                    }
                    else
                        error("Error in grammar (stat) after read( with " + look);
                    break;
                case Tag.IF:
                    int Itrue=code.newLabel();
                    int Ifalse=code.newLabel();
                    match(Tag.IF);
                    b_expr(Itrue,Ifalse);
                    match(Tag.THEN);
                    code.emitLabel(Itrue);
                    stat(lnext);
                    code.emit(OpCode.GOto,lnext);
                    if(look.tag==Tag.ELSE){
                        match(Tag.ELSE);
                        code.emitLabel(Ifalse);
                        stat(lnext);
                    }else
                        code.emitLabel(Ifalse);
                    break;
                case Tag.FOR:
                    match(Tag.FOR);
                    match('(');
                    int for_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (for_id_addr == -1) {
                        for_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match('=');
                    expr();
                    code.emit(OpCode.istore, for_id_addr);
                    match(';');
                    int begin = code.newLabel();
                    int ltrue = code.newLabel();
                    int lfalse = lnext;
                    int next = code.newLabel();
                    code.emitLabel(begin);
                    b_expr(ltrue, lfalse);
                    match(')');
                    match(Tag.DO);
                    code.emitLabel(ltrue);
                    stat(next);
                    code.emitLabel(next);
                    code.emit(OpCode.iload, for_id_addr);
                    code.emit(OpCode.ldc, 1);
                    code.emit(OpCode.iadd);
                    code.emit(OpCode.istore, for_id_addr);
                    code.emit(OpCode.GOto, begin);
                    break;
                case Tag.BEGIN:
                    match(Tag.BEGIN);
                    statlist(lnext);
                    match(Tag.END);
                    break;
                default:
                    error("syntax error");          
            }
        }else
            error("syntax error in stat");
    }
    
    private void  statlist(int lnext) {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN) {
            int lnext_statlist = code.newLabel();
            stat(lnext);
            code.emitLabel(lnext_statlist);
            statlistp(lnext);
        } else
            error("syntax error in statlist");
    }

    private void  statlistp(int lnext) {
        if(look.tag==';' || look.tag==Tag.END || look.tag==Tag.EOF){
            if(look.tag==';'){
                match(';');
                int stat_lnext=code.newLabel();
                stat(stat_lnext);
                code.emitLabel(stat_lnext);
                statlistp(lnext);
            }else if(look.tag==Tag.END || look.tag==Tag.EOF)
                return;
        }else
            error("syntax error in statlistp");
    }

    private void b_expr(int ltrue, int lfalse) {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();	    
            if (look == Word.eq) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmpeq, ltrue);
                 code.emit(OpCode.GOto, lfalse);
            } else if (look == Word.le) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmple, ltrue);
                 code.emit(OpCode.GOto,lfalse);
            } else if (look == Word.lt) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmplt, ltrue);
                 code.emit(OpCode.GOto,lfalse);
            } else if (look == Word.ne) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmpne, ltrue);
                 code.emit(OpCode.GOto,lfalse);
            } else if (look == Word.ge) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmpge, ltrue);
                 code.emit(OpCode.GOto,lfalse);
            } else if (look == Word.gt) {
                 match(Tag.RELOP);
                 expr();
                 code.emit(OpCode.if_icmpgt, ltrue);
                 code.emit(OpCode.GOto,lfalse);
            } else
                error("syntax error");
        } else
             error("syntax error in bexpr ");
    }

    private void expr() {
	if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
		term();
		exprp();
	} else
		error("syntax error in expr");
    }

    private void exprp() {
        if(look.tag=='+' || look.tag=='-' || look.tag==')' || look.tag==Tag.RELOP || look.tag==Tag.THEN || look.tag==Tag.ELSE || look.tag==';' || look.tag==Tag.END || look.tag==Tag.EOF){
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
   
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            fact();
            termp();
        } else
            error("syntax error in term");
    }

    private void termp() {
        if(look.tag=='*' || look.tag=='/' || look.tag=='+' || look.tag=='-' || look.tag==')' || look.tag==Tag.RELOP || look.tag==Tag.THEN || look.tag==Tag.ELSE || look.tag==';' || look.tag==Tag.END || look.tag==Tag.EOF){
            switch (look.tag) {
                case '*':
                    match('*');
                    fact();
                    code.emit(OpCode.imul);
                    termp();
                    break;
                case '/':
                    match('/');
                    fact();
                    code.emit(OpCode.idiv);
                    termp();
                    break;        
           }	
        }else
            error("syntax error in termp");
    }

    private void fact() {
        if(look.tag=='(' || look.tag==Tag.NUM || look.tag==Tag.ID){
            switch (look.tag) {
                case '(':
                    match('(');
                    expr();
                    match(')');
                    break;
                case Tag.NUM:
                    code.emit(OpCode.ldc, ((NumberTok)look).lexeme);
                    match(Tag.NUM);
                    break;
                case Tag.ID:
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                        if (id_addr == -1) {
                        error("Error! Uninitialized variable: " + ((Word)look).lexeme);
                        }
                    code.emit(OpCode.iload, id_addr);
                    match(Tag.ID);
                    break;
                default:
                    error("syntax error");
            }
        }else
            error("syntax error in fact");
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Prova.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }    
}