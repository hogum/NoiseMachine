import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class SoundsServer {

    List clientOutputStreams;

    public void startNoise() {

        clientOutputStreams = new ArrayList<ObjectOutputStream>();

        try {

            ServerSocket serverSocket = new ServerSocket(5080);

            while(true) {

                Socket clientSock = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());
                clientOutputStreams.add(out);
                
                ClientHandler handler = new ClientHandler("Client", clientSock);

                handler.start();
                System.out.println("Connection Established");
            }
        
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }


    public class ClientHandler implements Runnable {

        ObjectInputStream input;
        Thread thread;
        Socket socket;
        String threadName;

        ClientHandler(String name, Socket sock) {

            this.socket = sock;
            this.threadName = name;
        }

        public void start() {
             System.out.print("Creating connection ");   

            try {

                input = new ObjectInputStream(socket.getInputStream());
                System.out.println("   Done!");
            
            } catch (Exception ex) {

                ex.printStackTrace();
            }

            if (thread == null) {
                thread = new Thread(this, threadName);
            }

            thread.start();
        }

        public void run() {

            Object firstO;
            Object secondO;

            try {

                while((firstO = input.readObject()) != null) {

                    System.out.println("Reading     " + (String) firstO);
                    secondO = input.readObject();
                    shoutMessages(firstO, secondO);
                }

            } catch (Exception ex) {

                    ex.printStackTrace();
            }
        }
    }


    public void shoutMessages(Object message, Object two) {

        Iterator it = clientOutputStreams.iterator();

        while(it.hasNext()) {

            try {

                ObjectOutputStream outStream = (ObjectOutputStream) it.next();
                outStream.writeObject(message);
                outStream.writeObject(two);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

     public static void main(String[] args) {
        
        new SoundsServer().startNoise();
    }
}