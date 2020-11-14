public class runnable1 implements Runnable {
  private int cnt;

  public void run() {
    cnt += 3;
  }

  public int get_cnt() {
    return this.cnt;
  }
}
