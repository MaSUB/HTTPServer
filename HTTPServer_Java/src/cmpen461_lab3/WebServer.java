package cmpen461_lab3;
import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class WebServer {

    public static void main(String argv[]) throws Exception {
	
        //  Port number that is being listend to for requests.
        int port = 6789;
        ServerSocket socket;
        Socket client;
        
        
        //  Trys to set the socket on the server
        //  if the socket cant be started, it will throw an IO exception.
        //  
        client = new Socket();
        socket = new ServerSocket(port);
        System.out.println("The server is listening on socket #:\t" + port + "\n");
            
       
        
        while(true){
            
            client = socket.accept();
            
            if(socket.isBound()){
                
                HttpRequest request = new HttpRequest(client);
                Thread thread = new Thread(request);
                thread.start();
                
            }
            
        }
        
        
    }
}
