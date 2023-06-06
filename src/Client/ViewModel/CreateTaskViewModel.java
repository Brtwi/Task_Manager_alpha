package Client.ViewModel;

import Client.Model.State;
import Client.View.ConnectedUsers;
import Client.View.CreateTask;
import Client.ViewModel.Network.Connection;
import Client.ViewModel.Thread.SendTaskThread;
import Shared.Commands;
import Shared.Model.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

public class CreateTaskViewModel
{
    private final CreateTask window;
    private final MainWindowViewModel mainViewModel;
    private final String user;
    private final ArrayList<File> addedFiles;

    public CreateTaskViewModel(CreateTask window, MainWindowViewModel mainWindowViewModel)
    {
        this.window = window;
        this.mainViewModel = mainWindowViewModel;
        this.user = mainWindowViewModel.getUser();
        addActions();
        addedFiles = new ArrayList<>();
    }

    public MainWindowViewModel getMainViewModel()
    {
        return mainViewModel;
    }

    public void setReceiverLabel(String text)
    {
        window.getReceiverLabel().setText(text);
    }

    public CreateTask getWindow()
    {
        return window;
    }

    public Task saveTask()
    {
        if(window.getTitleTextField().getText() == null || window.getTitleTextField().getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this.window, "Nie podano tytułu zadania");
        }
        else if(window.getDetailsTextArea().getText() == null || window.getDetailsTextArea().getText().isEmpty())
        {
            return new Task(
                    window.getTitleTextField().getText(),
                    "",
                    this.user,
                    State.DO_ZROBIENIA,
                    addedFiles,
                    addedFiles.size()

            );
        }
        else
        {
            return new Task(
                    window.getTitleTextField().getText(),
                    window.getDetailsTextArea().getText(),
                    this.user,
                    State.DO_ZROBIENIA,
                    addedFiles,
                    addedFiles.size()
            );
        }
        return null;
    }

    private void filesAdded(String filePath)
    {
        addedFiles.add(new File(filePath));
    }

    private void showUsersAction(ActionEvent actionEvent)
    {
        if(window.getReceiverLabel().getText().isEmpty())
        {
            new ConnectedUsers(this);
        }
    }

    private void cancelAction(ActionEvent actionEvent)
    {
        window.dispose();
    }

    private void addFilesAction(ActionEvent actionEvent)
    {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this.window);

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            filesAdded(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveTaskAction(ActionEvent actionEvent)
    {
        Task newTask = saveTask();
        if(newTask != null)
        {
            if(!window.getReceiverLabel().getText().isEmpty())
            {
                newTask.setReceiver(window.getReceiverLabel().getText());
            }
            else
            {
                newTask.setReceiver(mainViewModel.getUser());
            }

            mainViewModel.addTaskToLists(newTask);

            window.dispose();
        }
    }

    private void sendTaskAction(ActionEvent actionEvent)
    {
        if(window.getReceiverLabel() != null && !window.getReceiverLabel().getText().isEmpty())
        {
            Task newTask = saveTask();
            newTask.setReceiver(window.getReceiverLabel().getText());
            new SendTaskThread(newTask, window.getReceiverLabel().getText()).start();

            mainViewModel.addTaskToSent(newTask);
            mainViewModel.addTaskToAllTaskList(newTask);

            window.dispose();
        }
    }

    private void addActions()
    {
        window.getSaveButton().addActionListener(this::saveTaskAction);
        window.getAddFilesButton().addActionListener(this::addFilesAction);
        window.getCancelButton().addActionListener(this::cancelAction);
        window.getSendButton().addActionListener(this::showUsersAction);
        window.getSendButton().addActionListener(this::sendTaskAction);


        window.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);

                synchronized (Connection.getClient().getLock())
                {
                    Connection.getClient().sendRequest(Commands.READY);
                    Connection.getClient().getLock().notify();
                }
            }
        });
        window.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);

                synchronized (Connection.getClient().getLock())
                {
                    Connection.getClient().sendRequest(Commands.READY);
                    Connection.getClient().getLock().notify();
                }
            }
        });
    }
}
