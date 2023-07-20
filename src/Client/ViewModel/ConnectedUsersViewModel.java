package Client.ViewModel;

import Client.View.ConnectedUsers;
import Client.Thread.RefreshUsersListThread;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectedUsersViewModel
{
    private final JList usersView;
    private final ConnectedUsers window;
    private final CreateTaskViewModel parentViewModel;
    private final RefreshUsersListThread thread1;


    public ConnectedUsersViewModel(ConnectedUsers window, CreateTaskViewModel parentViewModel)
    {
        this.window = window;
        this.parentViewModel = parentViewModel;
        this.usersView = window.getUsersList();

        thread1 = new RefreshUsersListThread(window.getUsersList(), parentViewModel.getMainViewModel().getUser());
        thread1.start();

        addActions();
    }

    public void addActions()
    {
        window.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                thread1.interrupt();
                window.dispose();
            }
        });

        window.getUsersList().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                if(e.getClickCount() == 1)
                {
                    if(usersView.getSelectedValue() != null)
                    {
                        parentViewModel.setReceiverLabel((String)usersView.getSelectedValue());
                        parentViewModel.getWindow().getSaveButton().setEnabled(false);
                    }
                    thread1.interrupt();

                    window.dispose();
                }
            }
        });
    }
}
