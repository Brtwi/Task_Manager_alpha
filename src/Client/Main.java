package Client;

import Client.View.MainWindow;
import Client.Network.Connection;

public class Main
{
    public static void main(String[] args)
    {
        Connection socketHandler = new Connection();
        socketHandler.start();

        MainWindow mainWindow = new MainWindow();
    }
}
