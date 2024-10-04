public class NumberTok extends Token {

    public int lexeme = 0;

    public NumberTok(int tag, int n) {
      super(tag);
      lexeme = n;
    }
    
    public String toString() {
      return "<" + tag + ", " + lexeme + ">";
    }

}
