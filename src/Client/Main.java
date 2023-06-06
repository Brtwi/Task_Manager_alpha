package Client;

import Client.View.MainWindow;
import Client.ViewModel.Network.Connection;

public class Main
{
    public static void main(String[] args)
    {
        Connection socketHandler = new Connection();
        socketHandler.start();

        MainWindow mainWindow = new MainWindow();
    }
}
