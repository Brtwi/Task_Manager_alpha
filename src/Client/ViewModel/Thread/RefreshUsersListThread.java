package Client.ViewModel.Thread;

import Client.ViewModel.Network.Client;
import Client.ViewModel.Network.Connection;
import Shared.Commands;

import javax.swing.*;
import java.util.Vector;

public class RefreshUsersListThread extends Thread
{
    private final JList usersList;
    private final Client client;
    private final String username;
    private final Integer lock;

    public RefreshUsersListThread(JList usersList, String user)
    {
        this.usersList = usersList;
        this.client = Connection.getClient();
        this.lock = client.getLock();
        this.username = user;
    }
    @Override
    public void run()
    {
        synchronized (lock)
        {
            try
            {
                Object response;
                while (!isInterrupted())
                {
                    client.sendRequest(Commands.USERS_LIST);

                    response = client.getResponse();
                    if (response instanceof Vector<?>)
                    {
                        ((Vector)response).remove(username);
                        usersList.setListData((Vector) response);
                    }

                    sleep(1500);
                }
            } catch (InterruptedException e)
            {
                System.out.println("Okno zamknięte");
            }
        }
    }
}
