
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;


public class MessagingServer {

    List clientOutputStreams;

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

             System.out.println("Creating connection");   

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

        public void run() {

            String msg;

            try {

                while((msg = bufReader.readLine()) != null) {

                    System.out.println("Reading     " + msg);
                    shoutMessages(msg);
                
                }

            } catch (Exception ex) {

                    ex.printStackTrace();
            }
        }

    }



    public void shoutMessages(String message) {

        Iterator it = clientOutputStreams.iterator();

        while(it.hasNext()) {

            try {

                PrintWriter pw = (PrintWriter) it.next();
                pw.println(message);
                pw.flush();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }



    public void startMessaging() {

        clientOutputStreams = new ArrayList();

        try {

            ServerSocket serverSocket = new ServerSocket(5880);

            while(true) {

                Socket clientSock = serverSocket.accept();
                PrintWriter pwrtr = new PrintWriter(clientSock.getOutputStream());
                clientOutputStreams.add(pwrtr);
                
                ClientHandler handler = new ClientHandler("Client", clientSock);

                handler.start();
                System.out.println("Connection Established");
            }
        
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
