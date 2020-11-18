import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    List<String> str = Collections.synchronizedList(new ArrayList<>());
    str.add("asdf");
    ArrayList<String> as = new ArrayList<>();
    hoge fuga = new hoge(str);
    Thread t[] = new Thread[10];
    for (int i = 0; i < 10; ++i)
      t[i] = new Thread(fuga);
    for (int i = 0; i < 10; ++i)
      t[i].start();
    for (int i = 0; i < 10; ++i)
      t[i].join();
    fuga.getAry().forEach((s) -> {
      System.out.println(s);
    });
  }
}

class hoge implements Runnable {
  private List<String> ary;

  public hoge(List<String> a) {
    this.ary = a;
  }

  public void run() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      String s = this.ary.get(0);
      String t = br.readLine();
      String st = s+t;
      System.out.println(st);
      this.ary.set(0,st);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<String> getAry() {
    return this.ary;
  }
}