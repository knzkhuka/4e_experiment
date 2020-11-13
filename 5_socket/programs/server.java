import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;

public class server {
  public static String question(Socket socket,String str) throws IOException{
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    writer.println(str+" : ");
    writer.flush();
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String line = reader.readLine();
    if(line.equals("end"))return null;
    return line;
  }
  public static void main(String[]args) throws IOException{
    
    int port = 1145;
    ServerSocket server = new ServerSocket(port);
    Socket socket = server.accept();

    String[] queries = {"Number","  Name","Score1","Score2","Score3","Score4"};
    Pattern seiseki_ptn = Pattern.compile(":([0-9]+):([a-zA-Z]+):([0-9]+):([0-9]+):([0-9]+):([0-9]+)");

    ArrayList<seiseki> datas = new ArrayList<>();

    ALL:while(true){
      String seiseki_str = new String();
      for(String query:queries){
        String data = question(socket, query);
        if(data==null)break ALL;
        seiseki_str += ":"+data;
        System.out.println(query+" : "+data);
      }

      System.out.println(seiseki_str);
      Matcher seiseki_mc = seiseki_ptn.matcher(seiseki_str);
      if(seiseki_mc.matches()){
        System.out.println("macthed!");
        int number = Integer.parseInt(seiseki_mc.group(1));
        String name = seiseki_mc.group(2);
        int[] scores = {Integer.parseInt(seiseki_mc.group(3)),Integer.parseInt(seiseki_mc.group(4)),Integer.parseInt(seiseki_mc.group(5)),Integer.parseInt(seiseki_mc.group(6))};
        seiseki tmp = new seiseki(number,name,scores);
        System.out.println("add : "+tmp.get_str());
        datas.add(tmp);
      }else{
        System.out.println("invalid");
      }

    }
    socket.close();
    server.close();

    System.out.println("added data");
    for(seiseki s:datas){
      System.out.println(s.get_str());
    }
  }
  public static class seiseki{
    public int number;
    public String name;
    public int[] scores;
    public double average;

    public seiseki(int _num,String _name,int[] _scores){
      this.number = _num;
      this.name = _name;
      this.scores = _scores;
      this.average = Arrays.stream(_scores).average().orElse(0);
    }
    public String get_str(){
      String nums = String.valueOf(this.number);
      String nams = "\t"+this.name;
      String scos = new String();
      for(int s:scores)scos+="\t"+String.valueOf(s);
      return nums+nams+scos;
    }
  }
}
