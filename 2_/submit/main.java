import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.Collections;

public class main{
  public static void print_data(ArrayList<ushi> data){
    for(ushi elm:data){
      elm.out();
    }
  }
  public static void print_by_stack(ArrayList<ushi> data){
    Stack<ushi> st = new Stack<>();
    for(ushi elm:data)st.push(elm);
    while(!st.empty()){
      st.peek().out();
      st.pop();
    }
  }
  public static void print_by_map(ArrayList<ushi> data){
    HashMap<String,ushi> mp = new HashMap();
    ArrayList<String> names = new ArrayList();
    for(ushi elm:data){
      mp.put(elm.get_name(),elm);
      names.add(elm.get_name());
    }
    Collections.sort(names);
    for(String name:names)mp.get(ave).out();
  }
  public static void print_data_with_average(ArrayList<ushi> data){
    for(ushi elm:data){
      elm.out_ave();
    }
  }
  public static void print_sorted_average(ArrayList<ushi> data){
    Collections.sort(data,new ushi_comparator());
    for(ushi elm:data)elm.out_ave();
  }
  public static void print_mach_initial(ArrayList<ushi> data,char c){
    for(ushi elm:data){
      if(elm.get_name().charAt(0) == c)elm.out();
    }
  }
  public static void main (String[] args){
    ushi aa = new ushi(1,"Michael",new ArrayList<>(Arrays.asList(75,86,89,31)));
    ushi bb = new ushi(2,"Emma",new ArrayList<>(Arrays.asList(74,63,70,48)));
    ushi cc = new ushi(3,"Hannah",new ArrayList<>(Arrays.asList(73,45,82,31)));
    ushi dd = new ushi(4,"Emily",new ArrayList<>(Arrays.asList(40,30,49,48)));
    ushi ee = new ushi(5,"Daniel",new ArrayList<>(Arrays.asList(46,59,47,70)));
    ArrayList<ushi> data = new ArrayList<>(Arrays.asList(aa,bb,cc,dd,ee));

    print_data(data);
    System.out.println();
    print_by_stack(data);
    System.out.println();
    print_by_map(data);
    System.out.println();
    print_data_with_average(data);
    System.out.println();
    print_sorted_average(data);
    System.out.println();
    print_mach_initial(data,'E');
  }
}