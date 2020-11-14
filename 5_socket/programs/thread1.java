public class thread1 extends Thread {
  private int cnt;
  public void run() {
    System.out.println(getName()+" : "+cnt);
    for(int i=0;i<10;++i)cnt+=i;
    System.out.println(getName()+" : "+cnt);
  }
}