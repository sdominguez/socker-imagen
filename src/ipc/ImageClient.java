
package ipc;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.Socket;


public class ImageClient {
    
    public static void main(String[] args) {
        String SERVER_IP = "localhost";
        int SERVER_PORT = 65489;
        
        try(Socket s = new Socket(SERVER_IP, SERVER_PORT);
                DataInputStream in = new DataInputStream(s.getInputStream())){
            
            String fileName = in.readUTF();
            long fileLength = in.readLong();
            
            System.out.println("Recibiendo imagen");
            
            try(FileOutputStream fout = new FileOutputStream("imagen_recibida.jpg")){
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;
                
                while(totalBytesRead < fileLength){
                    bytesRead = in.read(buffer, 0, 
                            (int)Math.min(buffer.length, fileLength - totalBytesRead));
                    fout.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }
                System.out.println("Transmision completa.");
            }catch(Exception ex){}
            
        }catch(Exception ex){}
    }
    
}
