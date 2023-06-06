package Client.ViewModel;

import Client.View.TaskDetails;
import Client.ViewModel.Network.Connection;
import Client.ViewModel.Thread.DownloadThread;
import Shared.Commands;

import java.awt.event.ActionEvent;

public class TaskDetailsViewModel
{
    private final TaskDetails window;

    public TaskDetailsViewModel(TaskDetails window)
    {
        this.window = window;
        addActions();
    }

    public void addActions()
    {
        window.getCloseButton().addActionListener(this::closeAction);
        window.getDownloadButton().addActionListener(this::downloadAction);
        window.getEditButton().addActionListener(this::editAction);
        window.getSaveButton().addActionListener(this::saveAction);
    }

    private void editAction(ActionEvent actionEvent)
    {
        window.getSaveButton().setEnabled(true);
        window.getTitleTextField().setEnabled(true);
        window.getDetailsTextArea().setEnabled(true);
        window.getStateComboBox().setEnabled(true);
    }

    private void downloadAction(ActionEvent actionEvent)
    {
        if(!window.getDownloadButton().getText().equals("0"))
        {
            Connection.getClient().sendRequest(Commands.GIVE_ME_FILES);
            Connection.getClient().sendRequest(window.getSelectedTask().getTitle());
            Connection.getClient().sendRequest(window.getSelectedTask().getDescription());

            new DownloadThread(window.getSelectedTask().getFileAmount()).start();

            window.getSelectedTask().setFileAmount(0);
            window.getDownloadButton().setText("0");
        }
    }

    private void closeAction(ActionEvent actionEvent)
    {
        window.dispose();
    }

    private void saveAction(ActionEvent actionEvent)
    {
        window.getSelectedTask().setTitle(window.getTitleTextField().getText());
        window.getSelectedTask().setDescription(window.getDetailsTextArea().getText());
        window.getSelectedTask().setStatus(window.getStateComboBox().getSelectedItem().toString());
        window.getSelectedTask().setFileAmount(Integer.parseInt(window.getDownloadButton().getText()));


        if(!window.getReceiverTextField().getText().equals(window.getProviderTextField().getText()))
        {
            Connection.getClient().sendRequest(Commands.UPDATE);

            if(window.getReceiverTextField().getText().equals(window.getOwner().getViewModel().getUser()))
            {
                Connection.getClient().sendRequest(window.getSelectedTask().getCreator());
            }
            else
            {
                Connection.getClient().sendRequest(window.getSelectedTask().getReceiver());
            }

            Connection.getClient().sendRequest(window.getSelectedTask().getID());

            Connection.getClient().sendInfo(window.getSelectedTask().getTitle());
            Connection.getClient().sendInfo(window.getSelectedTask().getDescription());
            Connection.getClient().sendInfo(window.getSelectedTask().getStatus());
        }

        Connection.getClient().sendRequest(Commands.OK);

        synchronized (Connection.getClient().getLock())
        {
            Connection.getClient().getLock().notify();
        }


        window.getOwner().getViewModel().setupLists();
        window.dispose();
    }
}
