public class runnable1 implements Runnable {
  public void run() {
    String thread_name = Thread.currentThread().getName();
    System.out.println(thread_name);
  }
}
