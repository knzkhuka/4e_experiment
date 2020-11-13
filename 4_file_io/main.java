import java.io.*;
import java.util.regex.*;
public class main{
  public static void main(String args[]){
    int N = 4;
    String regex = "([0-9]+) \\t ([a-zA-Z]+)";
    for(int i=0;i<N;++i)
      regex += "\\t ([0-9]{1,2}|100)";
    Pattern ptn = Pattern.compile(regex);
  }
}