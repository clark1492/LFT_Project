import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; 
        }
    }

    public Token lexical_scan(BufferedReader br) {
        int state=0;
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                readch(br);
                if (peek == '*'){
                    readch(br);
                    boolean b = false;
                    while (peek != '/' && !b){
                        if (peek == '*')
                            b = true;
                        else if (peek == (char) -1){
                            System.err.println("Erroneous note not ended");
                            return null;
                        }else
                            b = false;
                        readch(br);
                    }
                    peek=' ';
                    return lexical_scan(br);
                } else if (peek == '/'){
                    boolean c = true;
                    while (peek != '\n' && c){
                        readch(br);
                        if (peek == '\n' || peek == (char)-1)
                            c = false;
                    }
                    return lexical_scan(br);
                } else 
                    return Token.div;
		  
			case ';':
                peek = ' ';
                return Token.semicolon;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character" + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') { 
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else
                    return Word.lt;
                }

            case '>':
                readch(br);                    
                } if (peek == '=') { 
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') { 
                    peek = ' ';
                    return Word.eq;
                } else{
                    return Token.assign;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {
                    String s = new String();
        			while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'){
        				s += peek;
        				readch(br);
        			}
                    int i = 0;
                    while (state >= 0 && i < s.length()){
                        final char ch = s.charAt(i++);
                        switch (state){
                            case 0:
                                if (ch == '_')
                                    state = 1;
                                else if (Character.isLetter(ch))
                                    state = 2;
                                else
                                    state = -1;
                                break;
                            case 1:
                                if (Character.isLetterOrDigit(ch))
                                    state = 2;
                                else if (ch == '_')
                                    state = 1;
                                else
                                    state = -1;
                                break;
                            case 2:
                                if (Character.isLetterOrDigit(ch))
                                    state = 2;
                                else if (ch == '_')
                                    state = 2;
                                else
                                    state = -1;
                                break;
                        }
                    }
                    if (state == 1){
                        System.err.println("errore");
                        return null;
                    }

                    if (state == 2){
                        if (s.equals("if")){
        					return Word.iftok;
        				} else if(s.equals("then")){
        				    return Word.then;
        				} else if(s.equals("else")){
        				    return Word.elsetok;
        				} else if(s.equals("for")){
                            return Word.fortok;
        				} else if(s.equals("do")){
        				    return Word.dotok;
                        } else if(s.equals("read")){
                            return Word.read;
          				} else if(s.equals("print")){
                            return Word.print;
          				} else {
        				    return new Word(257, s); 
        					}
                    } else {
                        System.err.println("Erroneus state");
                        return null;
                    }
                } else if (Character.isDigit(peek)) {
                    String numero = new String();
						while(Character.isDigit(peek)){
							numero=numero + peek;
							readch(br);
							if(Character.isLetter(peek)||peek=='_'){
								System.err.println("Erroneus character: the number can't be at start of expression to be an identifier");
								return null;
							}
						}			
					int num= Integer.parseInt(numero);
                    return new NumberTok(Tag.NUM, num);
                } else {
                    System.err.println("Erroneous character: " + peek );
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Prova.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}