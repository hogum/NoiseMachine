
import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.net.SocketStream;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class MessagingServer {

    List clientOutputStream;

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Thread thread;
        Socket socket;
        String threadName;

        ClientHandler(String name, Socket sock) {

            this.socket = sock;
            this.threadName = name;
        }

        public void start() {

            try {

            }

            if (thread == null) {
                thread = new Thread(this, threadName);
            }

            thread.start();
        }

    }
}