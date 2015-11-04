package cmpen461_lab3;

import java.io.* ;
import java.net.* ;
import java.util.* ;

final class HttpRequest implements Runnable {

    final static String CRLF = "\r\n";
    Socket socket;
    
    HttpRequest(Socket socket) throws Exception {
        
        this.socket = socket;
        
    }
    
    @Override
    public void run() {
        
        try{
            
            processRequest();
            
        }
        catch(Exception e){
            
            System.out.println(e);
            
        }
        
        
    }
    
    private void processRequest() throws Exception {
        
        InputStream is;
        DataOutputStream os;
        BufferedReader br;
        
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        
        
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String requestLine = br.readLine();
        
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();
        String fileName = tokens.nextToken();
        fileName = "." + fileName;
        
        FileInputStream fis = null;
        boolean fileExists = true;
        
        try{
          
            fis = new FileInputStream(fileName);
            
        } 
        catch(FileNotFoundException e) {
            
            fileExists = false;
            
        }
        
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
            statusLine = "HTTP/1.1 200 OK"; 
            contentTypeLine = "Content-type: " +
            contentType(fileName) + CRLF;
        } else {
            statusLine = "HTTP/1.1 404\n";
            contentTypeLine = "Not Found\n";
            entityBody = "<HTML>\n" +
            "\t<HEAD>\n\t\t<TITLE>Not Found</TITLE>\n\t</HEAD>" +
            "\n\t<BODY>404 File Not Found</BODY>\n</HTML>";
        }


        os.writeBytes(statusLine);
        os.writeBytes(contentTypeLine);
        
        os.writeBytes(CRLF);
        String headerLine = null;
        
        if(fileExists){
            
            sendBytes(fis,os);
            fis.close();
            
        }
        
        else{
            
            os.writeBytes(entityBody);
            
        }
        while((headerLine = br.readLine()).length() != 0) {
            
            System.out.println(headerLine);
            
        }
       
        
        //
        //  Close all of the streams
        //
        is.close();
        os.close();
        br.close();
        socket.close();
        
    }
    
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
    {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        
        // Copy requested file into the socket's output stream.
        while((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName)
    {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            
            return "text/html";
            
        }
        if(fileName.endsWith(".gif")){
                
            return "image/gif";
            
        }
        if(fileName.endsWith(".jpg")){
            
            return "image/jpeg";
                
        }
        
        return "application/octet-stream";
        
    }
    
}

