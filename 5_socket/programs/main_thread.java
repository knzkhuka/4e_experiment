public class main_thread {
  public static void main(String[] args) {
    thread1 ushi = new thread1();
    thread2 tapu = new thread2();
    ushi.start();
    tapu.start();
  }
}
