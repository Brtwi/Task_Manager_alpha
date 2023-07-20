package Server;

import Server.Thread.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public static final int PORT = 2002;
    private static final ServerSocket serverSocket = null;

    public void start()
    {
        System.out.println("Starting server");

        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while(true)
            {
                System.out.println("Waiting for clients...");
                Socket socket = serverSocket.accept();

                System.out.println("New Client");

                //Socket do przesyłania plików
                Socket socketFile = serverSocket.accept();

                //Wątek dla klienta
                new ClientHandler(socket, socketFile).start();
            }
        } catch (IOException e)
        {
            System.out.println("Błąd przy nawiązywaniu połączenia z klientem");
        }
        finally
        {
            try
            {
                serverSocket.close();
            } catch (IOException e)
            {
                System.out.println("Błąd przy zamykaniu serwera");
            }
        }
    }

}
