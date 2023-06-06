package Client.View;

import Client.ViewModel.ConnectedUsersViewModel;
import Client.ViewModel.CreateTaskViewModel;

import javax.swing.*;

public class ConnectedUsers extends JDialog
{
    private JPanel contentPane;
    private JList usersList;

    public ConnectedUsers(CreateTaskViewModel parentViewModel)
    {
        setContentPane(contentPane);
        setModal(true);

        new ConnectedUsersViewModel(this, parentViewModel);

        setSize(500, 400);
        setVisible(true);
    }

    public JList getUsersList()
    {
        return usersList;
    }
}
