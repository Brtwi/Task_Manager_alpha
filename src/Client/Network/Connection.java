package Client.Network;

import Shared.Commands;

import java.io.*;
import java.net.Socket;

public class Connection
{
    private final static int PORT = 2002;
    private static final String SERVER = "localhost";
    private static Client client;

    public void start()
    {
        try
        {
            Socket socket = new Socket(SERVER, PORT);
            Socket fileSocket = new Socket(SERVER, PORT);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


            DataOutputStream dos = new DataOutputStream(fileSocket.getOutputStream());
            DataInputStream dis = new DataInputStream(fileSocket.getInputStream());


            client = new Client(socket, in, out, fileSocket, dos, dis);

            client.sendRequest(Commands.HELLO);

        } catch (IOException e)
        {
            System.out.println("Error while getting connection");
        }
    }

    public static Client getClient()
    {
        return client;
    }
}
