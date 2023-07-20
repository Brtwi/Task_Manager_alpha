package Client.Network;

import Shared.Services.FileService;
import Shared.Model.Task;

import java.io.*;
import java.net.Socket;

public class Client
{
    private final Socket socket;
    private final Socket fileSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private static final Integer LOCK = 1212;

    public Client(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream, Socket socketFile, DataOutputStream dos, DataInputStream dis)
    {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.fileSocket = socketFile;
        this.dataInputStream = dis;
        this.dataOutputStream = dos;
    }



    public void sendRequest(String request)
    {
        try
        {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Error while sending request");
        }
    }


    public void sendInfo(String info)
    {
        try
        {
            outputStream.writeUTF(info);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Error while sending UTF");
        }
    }


    public void sendTask(Task task)
    {
        try
        {
            outputStream.writeObject(task);
            outputStream.flush();

            if(task.getFileAmount() > 0)
            {
                for(File f : task.getFiles())
                {
                    sendFile(f);
                }
            }

        } catch (IOException e)
        {
            System.out.println("Error while sending task");
        }
    }

    public void sendFile(File fileToSend)
    {
        try
        {
            String fileName = fileToSend.getName();
            dataOutputStream.writeUTF(fileName);

            FileInputStream fis = new FileInputStream(fileToSend);
            byte[] fileData = new byte[(int) fileToSend.length()];
            dataOutputStream.writeInt(fileData.length);

            fis.read(fileData);

            dataOutputStream.write(fileData);
            dataOutputStream.flush();

        } catch (IOException e)
        {
            System.out.println("Error while sending files");
        }
    }


    public String readMessage()
    {
        try
        {
            return inputStream.readUTF();
        } catch (IOException e)
        {
            System.out.println("Error while reading message");
            return null;
        }
    }

    public void receiveFiles()
    {

        try
        {
            String fileName = dataInputStream.readUTF();
            int fileSize = dataInputStream.readInt();
            byte[] fileData = new byte[fileSize];
            dataInputStream.readFully(fileData);

            FileService.createDirectory("Klient");
            FileService.createFile(fileName, fileData);


        } catch (IOException e)
        {
            System.out.println("Error while downloading files");
            e.printStackTrace();
        }
    }

    synchronized public Object getResponse()
    {
        try
        {
            return inputStream.readObject();

        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Error while reading object " + e.getMessage());
        }
        return null;
    }

    public void disconnect()
    {
        sendRequest("STOP");
        try
        {

            socket.shutdownInput();
            socket.shutdownOutput();

            outputStream.close();
            inputStream.close();
            socket.close();

            fileSocket.shutdownInput();
            fileSocket.shutdownOutput();

            dataInputStream.close();
            dataInputStream.close();
            fileSocket.close();
        } catch (IOException e)
        {
            System.out.println("Error while closing connection");
        }
    }

    public Integer getLock()
    {
        return LOCK;
    }
}
