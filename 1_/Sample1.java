/**
 * 4EJ実験 最初のプログラム
 * @author ktajima
 */
public class Sample1 {

    public static void main(String[] args) {
        // The application start from this method.
        int apple = 3;
        int blueberry = 5;
        int sum = apple + blueberry;
        System.out.println("リンゴとブルーベリーの合計数は"+sum+"個でした。");
        
        int[] data = {47, 98, 39, 65, 71, 57, 77, 80, 90};
        
        int c = search(data,80);
        
        System.out.println("評価「優」の教科数は"+c+"個でした。");

    }

    public static int search(int[] data, int n) {
        int count = 0;
        for(int value:data){
            /*
            if(value == n){
                count++;
            }
            */
            if(value >= n){
                count++;
            }
        }
        return count;
    }
    
}