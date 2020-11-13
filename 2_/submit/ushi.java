import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;

public class ushi {
  private int Number;
  private String Name;
  private List<Integer> Scores;
  public ushi(int _num,String _name,List<Integer> _scores){
    this.Number = _num;
    this.Name = _name;
    this.Scores = _scores;
  }
  public int get_number(){return this.Number;}
  public String get_name(){return this.Name;}
  public List<Integer> get_scores(){return this.Scores;}
  public int get_average(){
    int sum = 0;
    int n = Scores.size();
    for(int s:Scores)sum+=s;
    return sum/n;
  }
  public void out(){
    String num = String.format("%2d",this.get_number());
    String name = String.format("%10s",this.get_name());
    System.out.print(num+name+" ");
    for(int s:this.get_scores())System.out.print(s+",");
    System.out.println();
  }
  public void out_ave(){
    String num = String.format("%2d",this.get_number());
    String name = String.format("%10s",this.get_name());
    System.out.print(num+name+" ");
    for(int s:this.get_scores())System.out.print(s+",");
    System.out.println(" "+this.get_average());
  }
}
