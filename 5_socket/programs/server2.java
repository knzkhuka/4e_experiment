import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class server2 {

  public static String question(Socket socket, String query) throws IOException {
    PrintWriter soket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    soket_writer.println(query + " : ");
    soket_writer.flush();
    BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String line = socket_reader.readLine();
    return line;
  }

  public static String questions(Socket socket, String[] queries) throws IOException {
    String result = new String();
    for (String query : queries) {
      String data = question(socket, query);
      if (data.equals("end"))
        return null;
      result += ":" + data;
    }
    System.out.println("result \"" + result + "\"");
    return result;
  }

  public static seiseki parse_seiseki(String line, Pattern ptn) {
    Matcher seiseki_match = ptn.matcher(line);
    if (!seiseki_match.matches())
      return null;
    int number = Integer.parseInt(seiseki_match.group(1));
    String name = seiseki_match.group(2);
    int[] scores = { Integer.parseInt(seiseki_match.group(3)), Integer.parseInt(seiseki_match.group(4)),
        Integer.parseInt(seiseki_match.group(5)), Integer.parseInt(seiseki_match.group(6)) };
    seiseki tmp = new seiseki(number, name, scores);
    return tmp;
  }

  public static void main(String[] args) throws IOException {

    int port = 1145;
    ServerSocket server = new ServerSocket(port);
    Socket socket = server.accept();

    String path = "output.txt";
    File file = new File(path);
    String end = "end";
    String search = "search";
    String add = "add";
    String[] query_type = { search, add };
    String[] search_type = { "Number", "Name" };
    String[] add_queries = { "Number", "  Name", "Score1", "Score2", "Score3", "Score4" };
    Pattern seiseki_ptn = Pattern.compile(":([0-9]+):([a-zA-Z]+):([0-9]+):([0-9]+):([0-9]+):([0-9]+)");

    TreeMap<Integer, seiseki> all_datas = new TreeMap<>();
    TreeSet<String> name_set = new TreeSet<>();
    TreeSet<Integer> number_set = new TreeSet<>();
    for (seiseki elm : read_sieski(file)) {
      all_datas.put(elm.number, elm);
      name_set.add(elm.name);
      number_set.add(elm.number);
    }

    ALL: while (true) {

      String qqt = "select " + String.join(",", query_type);
      String type = question(socket, qqt);
      if (type.equals(end))
        break ALL;

      if (type.equals(add)) {
        String seiseki_str = questions(socket, add_queries);
        if (seiseki_str == null)
          break;
        seiseki tmp = parse_seiseki(seiseki_str, seiseki_ptn);
        String message = new String();
        if (tmp == null) {
          System.out.println("invalid seiseki input");
          message = "invalid seiseki input";
        } else if (name_set.contains(tmp.name)) {
          System.out.println("duplication name");
          message = "duplication name";
        } else if (number_set.contains(tmp.number)) {
          System.out.println("duplication number");
          message = "duplication number";
        } else {
          all_datas.put(tmp.number, tmp);
          name_set.add(tmp.name);
          number_set.add(tmp.number);
          message = "added data \"" + tmp.get_all() + "\"";
        }
        if (question(socket, message).equals(end))
          break ALL;
      } else if (type.equals(search)) {

      }
    }
    socket.close();
    server.close();

    PrintWriter writer = new PrintWriter(new FileOutputStream(file));
    for (seiseki s : all_datas.values()) {
      System.out.println(s.get_all());
      writer.println(s.get_str());
    }
    writer.flush();
    writer.close();

  }

  public static class seiseki {
    public int number;
    public String name;
    public int[] scores;
    public double average;

    public seiseki(int _num, String _name, int[] _scores) {
      this.number = _num;
      this.name = _name;
      this.scores = _scores;
      this.average = Arrays.stream(_scores).average().orElse(0);
    }

    public String get_all() {
      String number_s = String.format("%3d", this.number);
      String name_s = String.format("%10s", this.name);
      String score_s = new String();
      for (int s : this.scores)
        score_s += String.format("%4d", s);
      String average_s = String.format("%5.1f", this.average);
      return number_s + name_s + score_s + average_s;
    }

    public String get_str() {
      String nums = String.valueOf(this.number);
      String nams = "\t" + this.name;
      String scos = new String();
      for (int s : scores)
        scos += "\t" + String.valueOf(s);
      return nums + nams + scos;
    }
  }

  public static ArrayList<seiseki> read_sieski(File file) {
    Pattern ptn = Pattern
        .compile("([0-9]+)\\t([a-zA-Z]+)\\t([0-9]{1,2}|100)\\t([0-9]{1,2}|100)\\t([0-9]{1,2}|100)\\t([0-9]{1,2}|100)");
    ArrayList<seiseki> data = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      reader.readLine();
      String str = new String();
      while ((str = reader.readLine()) != null) {
        seiseki tmp = parse_seiseki(str, ptn);
        if (tmp != null)
          data.add(tmp);
      }
      reader.close();
    } catch (FileNotFoundException err) {
      System.out.println(err);
    } catch (IOException err) {
      System.out.println(err);
    }
    return data;
  }
}
