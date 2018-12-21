
import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.net.SocketStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;


public class MessagingServer {

    List clientOutputStream;

    public class ClientHandler implements Runnable {

        BufferedReader bufReader;
        Thread thread;
        Socket socket;
        String threadName;

        ClientHandler(String name, Socket sock) {

            this.socket = sock;
            this.threadName = name;
        }

        public void start() {

            try {

                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                bufReader = new BufferedReader(isReader);
            
            } catch (Exception ex) {

                ex.printStackTrace();
            }

            if (thread == null) {
                thread = new Thread(this, threadName);
            }

            thread.start();
        }

    }
}