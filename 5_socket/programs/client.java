import java.util.*;
import java.io.*;
import java.net.*;

public class client {
  public static void main(String[]args)throws IOException{
    String ip = "192.168.3.33";
    int port = 1145;
    Socket socket = new Socket(ip,port);
    while(true){
      // サーバーからのメッセージ(query)を受け取って標準出力(System.out)
      BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String query = socket_reader.readLine();
      System.out.print(query);
      
      //　標準入力から1行入力する
      BufferedReader console_reader = new BufferedReader(new InputStreamReader(System.in));
      String line = console_reader.readLine();

      // 入力された文字列をサーバーに送信
      PrintWriter socket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      socket_writer.println(line);
      socket_writer.flush();

      // endなら通信終了なのでbreak
      if(line.equals("end"))break;
    }
    socket.close();
  }
}
