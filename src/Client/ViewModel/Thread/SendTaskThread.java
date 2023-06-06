package Client.ViewModel.Thread;

import Client.ViewModel.Network.Client;
import Client.ViewModel.Network.Connection;
import Shared.Model.Task;

public class SendTaskThread extends Thread
{
    private final Client client;
    private final Task task;
    private final String receiver;
    public SendTaskThread(Task task, String receiver)
    {
        this.client = Connection.getClient();
        this.task = task;
        this.receiver = receiver;
    }
    @Override
    public void run()
    {
        client.sendRequest("NewTask");
        client.sendRequest(receiver);
        client.sendTask(task);
    }

}
