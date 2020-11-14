public class thread1 extends Thread {
  public void run() {
    try {
      Thread.sleep(500);
      for (int i = 0; i < 10; ++i) {
        System.out.println(getName() + " : " + i);
      }
    } catch (InterruptedException err) {
      err.printStackTrace();
    }
  }
}