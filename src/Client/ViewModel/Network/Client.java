package Client.ViewModel.Network;

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
            System.out.println("Błąd przy wysyłaniu request");
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
            System.out.println("Błąd przy wysyłaniu UTF");
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
            System.out.println("Błąd przy wysyłaniu zadania");
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
            System.out.println("Błąd przy wysyłaniu plików");
        }
    }

    public String readMessage()
    {
        try
        {
            return inputStream.readUTF();
        } catch (IOException e)
        {
            System.out.println("Błąd przy odczytywaniu wiadomości");
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


            File fileToSave = new File("C:\\Users\\brtwi\\Documents\\Studia\\IV semestr\\" +
                    "Programowanie zaawnsowanych aplikacji JAVA\\" + fileName);

            FileOutputStream fos = new FileOutputStream(fileToSave);
            fos.write(fileData);

            fos.close();
        } catch (IOException e)
        {
            System.out.println("Błąd przy pobieraniu plików");
        }
    }

    synchronized public Object getResponse()
    {
        try
        {

            return inputStream.readObject();

        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Błąd przy pobieraniu danych " + e.getMessage());
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
            System.out.println("Błąd przy zamykaniu połączenia");
        }
    }

    public Integer getLock()
    {
        return LOCK;
    }
}
