package Server.Model;

import Shared.Commands;
import Shared.Model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Client
{
    private final String username;
    private final ObjectOutputStream outputStream;
    private final List<File> fileToSend;
    private final List<Task> taskForClient;
    private final List<Task> unsentTaks;
    private final List<List<String>> unsentUpdate;
    private boolean usersListLock;

    public Client(String username, ObjectOutputStream outputStream)
    {
        this.username = username;
        this.outputStream = outputStream;
        this.fileToSend = new ArrayList<>();
        this.taskForClient = new ArrayList<>();
        this.unsentTaks = new ArrayList<>();
        this.unsentUpdate = new ArrayList<>();
        this.usersListLock = false;
    }

    public void sendMessage(String string)
    {
        try
        {
            outputStream.writeUTF(string);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Błąd przy wysyłaniu UTF");
        }
    }

    public void sendNewTaskRequest()
    {
        try
        {
            outputStream.writeObject(Commands.NEW_TASK);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Błąd przy wysyłaniu wiadomości NewTask");
        }
    }

    public void sendUpdateTaskRequest()
    {
        try
        {
            outputStream.writeObject(Commands.UPDATE);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Błąd przy wysyłaniu wiadomości Update");
        }
    }

    public void sendTask(Task task)
    {
        try
        {
            outputStream.writeObject(task);
            outputStream.flush();
        } catch (IOException e)
        {
            System.out.println("Błąd przy wysyłaniu taska");
        }
    }
    public void addTaskToList(Task newTask)
    {
        taskForClient.add(newTask);
    }

    public void addTaskToUnsent(Task task)
    {
        this.unsentTaks.add(task);
    }

    public void addUpdateToUnsent(List<String> update)
    {
        this.unsentUpdate.add(update);
    }

    public List<File> getFiles()
    {
        return fileToSend;
    }

    public String getUsername()
    {
        return username;
    }

    public List<Task> getTasks()
    {
        return taskForClient;
    }

    public boolean isUsersListLock()
    {
        return this.usersListLock;
    }

    public void setUsersListLock(boolean isLocked)
    {
        this.usersListLock = isLocked;
    }
    public List<Task> getUnsent()
    {
        return unsentTaks;
    }

    public List<List<String>> getUnsentUpdate()
    {
        return this.unsentUpdate;
    }
}
