package Server.Thread;

import Server.Helpers.ConvertData;
import Server.Model.Client;
import Shared.Model.Task;
import Shared.Commands;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;


public class ClientHandler extends Thread
{
    private final Socket socket;
    private final Socket fileSocket;
    private final static HashSet<Client> connectedClients = new HashSet<>();
    private Client client;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;

    public ClientHandler(Socket socket, Socket fileSocket)
    {
        this.socket = socket;
        this.fileSocket = fileSocket;

        try
        {
            inFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());

            dataInput = new DataInputStream(fileSocket.getInputStream());
            dataOutput = new DataOutputStream(fileSocket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println("Błąd przy otwieraniu strumieni");
        }
    }


    @Override
    public void run()
    {
        try
        {
            label:
            while (true)
            {
                String message = (String)inFromClient.readObject();
                System.out.println("Klient przysłał " + message);

                switch(message)
                {
                    case Commands.STOP:

                        outToClient.writeObject(Commands.STOP);

                        socket.shutdownInput();
                        socket.shutdownOutput();

                        inFromClient.close();
                        outToClient.close();
                        socket.close();

                        fileSocket.shutdownOutput();
                        fileSocket.shutdownInput();

                        dataInput.close();
                        dataOutput.close();
                        fileSocket.close();

                        connectedClients.remove(client);
                        break label;

                    case Commands.HELLO:

                        Vector<String> logins = new Vector<>();

                        //Wysłanie zajętych loginów
                        connectedClients.stream().map(Client::getUsername).forEach(logins::add);
                        outToClient.writeObject(logins);

                        //Odczytanie nowego loginu
                        message = (String)inFromClient.readObject();

                        //Utworzenie i dodanie nowego klienta
                        client = new Client(message, outToClient);
                        connectedClients.add(this.client);

                        System.out.println("Dodano do listy");
                        System.out.println(connectedClients.size() + " connected");

                        outToClient.writeObject(Commands.HELLO);
                        outToClient.flush();
                        break;

                    case Commands.USERS_LIST:
                        client.setUsersListLock(true);
                        outToClient.writeObject(ConvertData.convertToVector(connectedClients));
                        outToClient.flush();
                        break;

                    case Commands.NEW_TASK:

                        message = (String)inFromClient.readObject();
                        for (Client c : connectedClients)
                        {
                            if (c.getUsername().equals(message))
                            {
                                Task task = (Task) inFromClient.readObject();

                                if(c.isUsersListLock())
                                {
                                    c.addTaskToUnsent(task);
                                }
                                else
                                {
                                    c.sendNewTaskRequest();
                                    c.sendTask(task);
                                    System.out.println("Wysłano task do " + c.getUsername());
                                }
                                c.addTaskToList(task);

                                if(task.getFileAmount() > 0)
                                {
                                    receiveFiles(c);
                                }
                            }
                        }
                        break;

                    case Commands.GIVE_ME_FILES:

                        String titleTask = (String)inFromClient.readObject();
                        String descriptionTask = (String)inFromClient.readObject();

                        for(Task t : client.getTasks())
                        {
                            if(titleTask.equals(t.getTitle()) && descriptionTask.equals(t.getDescription()))
                            {
                                for(File f : t.getFiles())
                                {
                                    int j = 0;
                                    while(!f.getName().equals(client.getFiles().get(j).getName()))
                                    {
                                        j++;
                                    }
                                    sendFile(client.getFiles().get(j));
                                }
                            }
                        }
                        break;

                    case Commands.UPDATE:

                        outToClient.writeObject(Commands.WAIT);

                        String receiver = (String)inFromClient.readObject();
                        String taskID = (String)inFromClient.readObject();
                        String title = inFromClient.readUTF();
                        String description = inFromClient.readUTF();
                        String status = inFromClient.readUTF();


                        for(Client c : connectedClients)
                        {
                            if(c.getUsername().equals(receiver))
                            {
                                if(!c.isUsersListLock())
                                {
                                    c.sendUpdateTaskRequest();
                                    c.sendMessage(taskID);
                                    c.sendMessage(title);
                                    c.sendMessage(description);
                                    c.sendMessage(status);
                                }
                                else
                                {
                                    ArrayList<String> unsent =
                                            new ArrayList<>(List.of(receiver, taskID, title, description, status));
                                    c.addUpdateToUnsent(unsent);
                                }
                                for(Task t : c.getTasks())
                                {
                                    if(t.getID().equals(taskID))
                                    {
                                        t.setTitle(title);
                                        t.setDescription(description);
                                        t.setStatus(status);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        outToClient.writeObject(Commands.OK);
                        break;
                    case Commands.READY:

                        client.setUsersListLock(false);

                        for(Task t : client.getUnsent())
                        {
                            client.sendNewTaskRequest();
                            client.sendTask(t);
                        }
                        client.getUnsent().removeAll(client.getUnsent());

                        for(List<String> list : client.getUnsentUpdate())
                        {
                            client.sendUpdateTaskRequest();
                            for(String s : list)
                            {
                                client.sendMessage(s);
                            }
                        }
                        client.getUnsentUpdate().removeAll(client.getUnsentUpdate());

                        break;

                    case Commands.OK:
                        client.setUsersListLock(false);
                        outToClient.writeObject(Commands.OK);
                        outToClient.flush();
                        break;
                    case Commands.WAIT:

                        outToClient.writeObject(Commands.WAIT);
                        outToClient.flush();
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }  finally
        {
            try
            {
                inFromClient.close();
                outToClient.close();
                socket.close();

                dataInput.close();
                dataOutput.close();
                fileSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void receiveFiles(Client client)
    {

        try
        {
            String fileName = dataInput.readUTF();
            int fileSize = dataInput.readInt();
            byte[] fileData = new byte[fileSize];
            dataInput.readFully(fileData);


            File fileToSave = new File("C:\\Users\\brtwi\\Documents\\Studia\\IV semestr\\" +
                    "Programowanie zaawnsowanych aplikacji JAVA\\Serwerowe piki\\" + fileName);

            FileOutputStream fos = new FileOutputStream(fileToSave);
            fos.write(fileData);

            client.getFiles().add(fileToSave);

            fos.close();
        } catch (IOException e)
        {
            System.out.println("Błąd przy odbieraniu plików");
        }

    }

    public void sendFile(File fileToSend)
    {
        try
        {
            String fileName = fileToSend.getName();
            dataOutput.writeUTF(fileName);

            FileInputStream fis = new FileInputStream(fileToSend);
            byte[] fileData = new byte[(int) fileToSend.length()];

            dataOutput.writeInt(fileData.length);

            fis.read(fileData);

            dataOutput.write(fileData);
            dataOutput.flush();

            System.out.println("Wysłano plik");
        } catch (IOException e)
        {
            System.out.println("Błąd przy wysyłaniu plików");
        }
    }
}
