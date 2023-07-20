package Client.ViewModel;

import Client.View.CreateTask;
import Client.View.MainWindow;
import Client.View.TaskDetails;
import Client.Network.Connection;
import Client.Thread.TaskForMeThread;
import Shared.Commands;
import Shared.Model.Task;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class MainWindowViewModel
{
    private final MainWindow mainWindow;
    private final Vector<Task> allTasksListData = new Vector<>();
    private final Vector<Task> myTasksListData = new Vector<>();
    private final Vector<Task> otherTasksListData = new Vector<>();
    private final String user;
    private final TaskForMeThread thread;


    public MainWindowViewModel(MainWindow view)
    {
        this.mainWindow = view;
        addActions();
        setupLists();
        this.user = usernameDialog();

        thread = new TaskForMeThread(this);
    }


    public Vector<Task> getAllTasksListData()
    {
        return allTasksListData;
    }


    public String getUser()
    {
        return user;
    }


    public void setupLists()
    {
        mainWindow.getAllTaskList().setListData(allTasksListData);
        mainWindow.getMyTaskList().setListData(myTasksListData);
        mainWindow.getOtherTasksList().setListData(otherTasksListData);
    }


    private void addActions()
    {
        mainWindow.getAddTaskButton().addActionListener(this::addTaskAction);
        mainWindow.getDeleteTaskButton().addActionListener(this::deleteTaskAction);
        mainWindow.getAllTaskList().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    Task selectedValue = (Task)mainWindow.getAllTaskList().getSelectedValue();
                    if (selectedValue != null)
                    {
                        new TaskDetails(mainWindow, selectedValue);
                    }
                }
            }
        });

        mainWindow.getMyTaskList().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);

                if (e.getClickCount() == 2)
                {
                    Task selectedValue = (Task)mainWindow.getAllTaskList().getSelectedValue();
                    if (selectedValue != null)
                    {
                        new TaskDetails(mainWindow, selectedValue);
                    }
                }
            }
        });

        mainWindow.getOtherTasksList().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                if (e.getClickCount() == 2)
                {
                    Task selectedValue = (Task)mainWindow.getAllTaskList().getSelectedValue();
                    if (selectedValue != null)
                    {
                        new TaskDetails(mainWindow, selectedValue);
                    }
                }
            }
        });

        mainWindow.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                Connection.getClient().disconnect();

            }
        });
        mainWindow.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
                super.windowOpened(e);
                new Connection();
                Connection.getClient().sendRequest(user);
                System.out.println(Connection.getClient().getResponse());
                thread.start();
            }
        });
    }


    private void deleteTaskAction(ActionEvent actionEvent)
    {
        if(!mainWindow.getAllTaskList().isSelectionEmpty())
        {
            allTasksListData.remove(mainWindow.getAllTaskList().getSelectedIndex());
        }
        if(!mainWindow.getMyTaskList().isSelectionEmpty())
        {
            myTasksListData.remove(mainWindow.getMyTaskList().getSelectedIndex());
        }
        if(!mainWindow.getOtherTasksList().isSelectionEmpty())
        {
            otherTasksListData.remove(mainWindow.getOtherTasksList().getSelectedIndex());
        }
        setupLists();
    }


    private void addTaskAction(ActionEvent e)
    {
        Connection.getClient().sendRequest(Commands.WAIT);
        new CreateTask(mainWindow);
    }


    public void addTaskToLists(Task task)
    {
        allTasksListData.add(task);
        mainWindow.getAllTaskList().setListData(allTasksListData);
        myTasksListData.add(task);
        mainWindow.getMyTaskList().setListData(myTasksListData);
    }


    public void addTaskToAllTaskList(Task task)
    {
        allTasksListData.add(task);
        mainWindow.getAllTaskList().setListData(allTasksListData);
    }


    public void addTaskToSent(Task task)
    {
        otherTasksListData.add(task);
        mainWindow.getOtherTasksList().setListData(otherTasksListData);
    }


    private String usernameDialog()
    {
        String login =  "";
        Vector<String> usedLogins = (Vector<String>) Connection.getClient().getResponse();
        while(login == null || login.isEmpty())
        {
            login = JOptionPane.showInputDialog(this.mainWindow, "Enter your username");
            if(login == null || login.isBlank())
            {
                JOptionPane.showMessageDialog(this.mainWindow, "Please enter your username", "Missing data", JOptionPane.ERROR_MESSAGE);
            } else if (usedLogins.contains(login))
            {
                JOptionPane.showMessageDialog(this.mainWindow, "This username is already taken", "Unavailable username", JOptionPane.ERROR_MESSAGE);
                login = "";
            }
        }
        return login;
    }
}
