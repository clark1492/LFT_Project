import java.io.*;

public class Lexer {

  public static int line = 1;
  private char peek = ' ';

  private void readch(BufferedReader br) {
    try {
      peek = (char) br.read();
    } catch (IOException exc) {
      peek = (char) -1; // ERROR
    }
  }

  public Token lexical_scan(BufferedReader br) {
    int state = 0;
    while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
      if (peek == '\n')
        line++;
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
        peek = ' ';
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
          System.err.println("Erroneous character" + " after & : " + peek);
          return null;
        }

      case '|':
        readch(br);
        if (peek == '|') {
          peek = ' ';
          return Word.or;
        } else {
          System.err.println("Erroneous character" + " after | : " + peek);
          return null;
        }

      case '<':
        readch(br);
        if (peek == '=') { // '<' + '=' == '<=' quindi minore o uguale
          peek = ' ';
          return Word.le;
        } else if (peek == '>') { // '<' + '>' == '<>' quindi diverso
          peek = ' ';
          return Word.ne;
        } else if (peek == ' ') { // '<' + ' ' == '<' quindi minore
          peek = ' ';
          return Word.lt;
        } else {
          System.err.println("Erroneous character" + " after < : " + peek);
          return null;
        }

      case '>':
        readch(br);
        if (peek == ' ') { // maggiore
          peek = ' ';
          return Word.gt;
        } else if (peek == '=') { // '>=' maggiore o uguale
          peek = ' ';
          return Word.ge;
        } else {
          System.err.println("Erroneous character" + " after > : " + peek);
          return null;
        }

      case '=':
        readch(br);
        if (peek == '=') { // '=='
          peek = ' ';
          return Word.eq;
        } else if (peek == ' ') {
          peek = ' ';
          return Token.assign;
        } else {
          System.err.println("Erroneous character" + " after = : " + peek);
          return null;
        }

      case (char) -1:
        return new Token(Tag.EOF);

      default:

        if (Character.isLetter(peek) || peek == '_') {

          String s = new String();
          while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') { // da qui parte uno scan per le
                                                                                       // stringhe di parole chiave e id
            s += peek;
            readch(br);
          }

          int i = 0;
          while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
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
                  state = 3;
                else
                  state = -1;
                break;

              case 3:
                if (Character.isLetterOrDigit(ch))
                  state = 2;
                else if (ch == '_')
                  state = 3;
                else
                  state = -1;
                break;
            }
          }

          if (state == 1 || state == 3) {
            System.err.println("Erroneous identifier '" + s + "': cannot end with underscore");
            return null;
          }

          if (state == 2) {

            if (s.equals("if")) {
              return Word.iftok;
            } else if (s.equals("then")) {
              return Word.then;
            } else if (s.equals("else")) {
              return Word.elsetok;
            } else if (s.equals("for")) {
              return Word.fortok;
            } else if (s.equals("do")) {
              return Word.dotok;
            } else if (s.equals("read")) {
              return Word.read;
            } else if (s.equals("print")) {
              return Word.print;
            } else {
              return new Word(257, s); // se non è una parola chiave nei casi di prima è un identificatore
            }
          } else {
            System.err.println("Erroneus state");
            return null;
          }
        } else if (Character.isDigit(peek)) { // da qui parte la gestione dei numeri
          int n = 0;
          while (Character.isDigit(peek)) {
            int x = Character.getNumericValue(peek);
            n = (n * 10) + x;
            readch(br);
          }
          return new NumberTok(Tag.NUM, n);
        } else {
          System.err.println("Erroneous character: " + peek);
          return null;
        }
    }

  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = args[0]; // il percorso del file da leggere
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Token tok;
      do {
        tok = lex.lexical_scan(br);
        System.out.println("Scan: " + tok);
      } while (tok.tag != Tag.EOF);
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}