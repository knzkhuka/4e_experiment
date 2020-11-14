public class main_thread {
  public static void main(String[] args) {
    for (int i = 0; i < 10; ++i) {
      int n = 10;
      runnable1 r = new runnable1();
      Thread[] t = new Thread[n];
      for (int j = 0; j < n; ++j)
        t[j] = new Thread(r);
      for (int j = 0; j < n; ++j)
        t[j].start();

      System.out.println(i + " : " + r.get_cnt());
    }
  }
}
