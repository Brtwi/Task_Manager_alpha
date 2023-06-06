package Client.ViewModel.Thread;

import Client.ViewModel.MainWindowViewModel;
import Client.ViewModel.Network.Client;
import Client.ViewModel.Network.Connection;
import Shared.Commands;
import Shared.Model.Task;

public class TaskForMeThread extends Thread
{
    private final MainWindowViewModel mainWindowViewModel;
    private final Client client;
    private final Integer lock;

    public TaskForMeThread(MainWindowViewModel viewModel)
    {
        this.mainWindowViewModel = viewModel;
        client = Connection.getClient();
        this.lock = client.getLock();
    }
    @Override
    public void run()
    {
        try
        {
            Object response;
            String message = "";

            while(true)
            {
                synchronized (lock)
                {
                    response = client.getResponse();
                    if(response instanceof String)
                        message = ((String)response);

                    System.out.println("Serwer przysłał wiadomość: " + message);

                    switch (message)
                    {
                        case Commands.NEW_TASK ->
                        {
                            if ((response = client.getResponse()) instanceof Task)
                            {
                                ((Task) response).setReceiver(mainWindowViewModel.getUser());
                                mainWindowViewModel.addTaskToAllTaskList((Task) response);
                            }
                        }
                        case Commands.UPDATE ->
                        {
                            String taskID = client.readMessage();
                            for (Task t : mainWindowViewModel.getAllTasksListData())
                            {
                                if (t.getID().equals(taskID))
                                {
                                    t.setTitle(client.readMessage());
                                    t.setDescription(client.readMessage());
                                    t.setStatus(client.readMessage());
                                    mainWindowViewModel.setupLists();
                                    break;
                                }
                            }
                        }
                        case Commands.WAIT ->
                        {
                            lock.wait();
                        }
                    }

                }
                sleep(1000);
            }
        } catch (InterruptedException e)
        {
        }
        finally
        {
            if(client != null)
                client.disconnect();
        }
    }
}
