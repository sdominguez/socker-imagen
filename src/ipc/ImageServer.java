
package ipc;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ImageServer implements Runnable{
    
    private final Socket ch;
    private final String imagePath;
    
    public ImageServer(Socket clientSocket,String imagePath){
            this.ch = clientSocket;
            this.imagePath = imagePath;
            System.out.println("Cliente conectado desde "+
                    clientSocket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        try(DataOutputStream out = new DataOutputStream(ch.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(imagePath)))){
            out.writeUTF(new File(imagePath).getName());
            out.writeLong(new File(imagePath).length());
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = bis.read(buffer))!=-1){
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Trasmision de imagen completa.");
        }catch(Exception e){}finally{
            closeClient();
        }
    }
    
    private void closeClient(){
        try {
            ch.close();
        } catch (IOException ex) {
        }
    }
    
    public static void main(String[] args) {
        int PORT = 65489;
        try(ServerSocket ss = new ServerSocket(PORT)){
            System.out.println("Servidor esperando conexiones en puerto "+PORT);
            while(true){
                Thread t = new Thread(new ImageServer(ss.accept(), "C:\\Users\\sauld\\OneDrive\\Imágenes\\feilogo.png"));
                t.start();
            }
        }catch(Exception e){}
    }
    
    
    
}
