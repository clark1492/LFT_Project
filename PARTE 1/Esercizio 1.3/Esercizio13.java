public class Esercizio13{

  public static boolean scan(String s){

    int state = 0;
    int i = 0;

    while (state >= 0 && i<s.length()){
      final char ch = s.charAt(i++);

      switch(state){
        case 0:
          if (ch == '0' || ch == '8' || ch == '2' || ch == '4' || ch == '6'){
            state = 2;
          } else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9'){
            state = 1;
          } else {
            state = -1;
          }
          break;

          case 1:
            if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
              state = 1;
            else if (ch == '0' || ch == '8' || ch == '2' || ch == '4' || ch == '6')
              state = 2;
            else if (ch >= 'L' && ch <= 'Z')
              state = 3;
            else
              state = -1;
            break;

          case 2:
          if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
            state = 1;
          else if (ch == '0' || ch == '8' || ch == '2' || ch == '4' || ch == '6')
            state = 2;
          else if (ch >= 'A' && ch <= 'K')
            state = 3;
          else
            state = -1;
          break;

          case 3:
            if (ch >= 'a' && ch <= 'z')
              state = 3;
            else
              state = -1;
            break;
      }
    }
    return state == 3;
  }

  public static void main(String[] args){
      String a = args[0];
      for(int i = 1; i < args.length; i++)
        a += " " + args[i];
      System.out.println(scan(a) ? "OK" : "NOPE");
  }
}