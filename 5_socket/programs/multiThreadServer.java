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
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class multiThreadServer {
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
    String path = "output.txt";
    File file = new File(path);
    TreeMap<String, seiseki> name_data = new TreeMap<String, seiseki>();
    TreeMap<Integer, seiseki> number_data = new TreeMap<Integer, seiseki>();
    Map<String, seiseki> name_key_data = Collections.synchronizedSortedMap(name_data);
    Map<Integer, seiseki> number_key_data = Collections.synchronizedSortedMap(number_data);
    for (seiseki elm : read_sieski(file)) {
      number_key_data.put(elm.number, elm);
      name_key_data.put(elm.name, elm);
    }

    int port = 10101;
    ServerSocket server = new ServerSocket(port);

    while (true) {
      Socket socket = server.accept();
      new Thread(new seisekiServer(socket, name_key_data, number_key_data)).start();
    }

    server.close();
  }
}

class seisekiServer implements Runnable {

  Socket socket;
  Map<String, seiseki> name_key_data;
  Map<Integer, seiseki> number_key_data;

  public seisekiServer(Socket socket, Map<String, seiseki> a, Map<Integer, seiseki> b) {
    this.socket = socket;
    this.name_key_data = a;
    this.number_key_data = b;
  }

  public static String question(Socket socket, String query) throws IOException {
    PrintWriter soket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    soket_writer.println(query + " : ");
    soket_writer.flush();
    BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String line = socket_reader.readLine();
    return line;
  }

  public static String message(Socket socket, String query) throws IOException {
    PrintWriter soket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    soket_writer.println(query + "  (enter to next)");
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

  public void run() {

    String path = "output.txt";
    File file = new File(path);
    String end = "end";
    String search = "search";
    String add = "add";
    String list = "list";
    String[] query_type = { search, add, list };
    String[] search_type = { "Number", "Name" };
    String[] add_queries = { "Number", "  Name", "Score1", "Score2", "Score3", "Score4" };
    Pattern seiseki_ptn = Pattern.compile(":([0-9]+):([a-zA-Z]+):([0-9]+):([0-9]+):([0-9]+):([0-9]+)");

    ALL: while (true) {
      try {
        String qqt = "select " + String.join(",", query_type);
        String type = question(socket, qqt);
        if (type.equals(end))
          break ALL;

        if (type.equals(add)) {
          String seiseki_str = questions(socket, add_queries);
          if (seiseki_str == null)
            break;
          seiseki tmp = parse_seiseki(seiseki_str, seiseki_ptn);
          String result_message = new String();
          if (tmp == null) {
            System.out.println("invalid seiseki input");
            result_message = "invalid seiseki input";
          } else if (name_key_data.containsKey(tmp.name)) {
            System.out.println("duplication name");
            result_message = "duplication name";
          } else if (number_key_data.containsKey(tmp.number)) {
            System.out.println("duplication number");
            result_message = "duplication number";
          } else {
            number_key_data.put(tmp.number, tmp);
            name_key_data.put(tmp.name, tmp);
            result_message = "added data \"" + tmp.get_all() + "\"";
          }
          if (message(socket, result_message).equals(end))
            break ALL;
        }

        else if (type.equals(search)) {
          String qst = "select " + String.join(",", search_type);
          String stype = question(socket, qst);
          String result_message = "not found";
          if (stype.equals(end)) {
            break ALL;
          } else if (stype.equals("name") || stype.equals("Name")) {
            String search_name = question(socket, "enter name");
            if (search_name.equals(end))
              break ALL;
            if (name_key_data.containsKey(search_name))
              result_message = name_key_data.get(search_name).get_all();
          } else if (stype.equals("number") || stype.equals("Number")) {
            String search_number_str = question(socket, "enter number");
            if (search_number_str.equals(end))
              break ALL;
            Integer search_number = Integer.parseInt(search_number_str);
            if (number_key_data.containsKey(search_number))
              result_message = number_key_data.get(search_number).get_all();
          }
          if (message(socket, result_message).equals(end))
            break ALL;
        }

        else if (type.equals(list)) {
          PrintWriter socket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
          for (seiseki elm : number_key_data.values()) {
            socket_writer.println(elm.get_all());
          }
          socket_writer.println("endlist");
          socket_writer.flush();
          BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          socket_reader.readLine();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      PrintWriter writer = new PrintWriter(new FileOutputStream(file));
      writer.println("Number  Name  Score1  Score2  Score3  Score4");
      for (seiseki s : number_key_data.values()) {
        System.out.println(s.get_all());
        writer.println(s.get_str());
      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class seiseki {
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