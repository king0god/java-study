
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class client {
    public static void main(String[] args) {
        System.out.println("start");

        try {
            Socket socket = null;
            // 소켓 서버에 접속
            socket = new Socket("172.20.10.2", 8080);
            System.out.println("서버에 접속 성공!"); // 접속 확인용

            // 서버에서 보낸 메세지 읽는 Thread
            ListeningThread t1 = new ListeningThread(socket);
            WritingThread t2 = new WritingThread(socket); // 서버로 메세지 보내는 Thread

            t1.start(); // ListeningThread Start
            t2.start(); // WritingThread Start

        } catch (Exception e) {
            e.printStackTrace(); // 예외처리
        }
    }
}

class ListeningThread extends Thread { // 서버에서 보낸 메세지 읽는 Thread
    Socket socket = null;

    public ListeningThread(Socket socket) { // 생성자
        this.socket = socket; // 받아온 Socket Parameter를 해당 클래스 Socket에 넣기
    }

    public void run() {
        try {
            // InputStream - Server에서 보낸 메세지를 클라이언트로 가져옴
            // socket의 InputStream 정보를 InputStream in에 넣은 뒤
            InputStream input = socket.getInputStream();
            // BufferedReader에 위 InputStream을 담아 사용
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while(true) { // 무한반복
                System.out.println(reader.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class WritingThread extends Thread { // 서버로 메세지 보내는 Thread
    Socket socket = null;
    Scanner scanner = new Scanner(System.in); // 채팅용 Scanner

    public WritingThread(Socket socket) { // 생성자
        // 받아온 Socket Parameter를 해당 클래스 Socket에 넣기
        this.socket = socket;
    }

    public void run() {
        try {
            // OutputStream - 클라이언트에서 Server로 메세지 발송
            // socket의 OutputStream 정보를 OutputStream out에 넣은 뒤
            OutputStream out = socket.getOutputStream();
            // PrintWriter에 위 OutputStream을 담아 사용
            PrintWriter writer = new PrintWriter(out, true);

            while(true) { // 무한반복
                writer.println(scanner.nextLine()); // 입력한 메세지 발송
            }

        } catch (Exception e) {
            e.printStackTrace(); // 예외처리
        }
    }
}
