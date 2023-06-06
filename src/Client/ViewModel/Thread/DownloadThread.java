package Client.ViewModel.Thread;

import Client.ViewModel.Network.Client;
import Client.ViewModel.Network.Connection;

public class DownloadThread extends Thread
{
    private final Client client;
    private Integer fileAmount;

    public DownloadThread(Integer fileAmount)
    {
        this.client = Connection.getClient();
        this.fileAmount = fileAmount;
    }

    @Override
    public void run()
    {
        while (fileAmount > 0)
        {
            client.receiveFiles();
            fileAmount--;
        }
    }
}
