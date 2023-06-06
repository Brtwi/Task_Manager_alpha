package Client.View;

import Client.ViewModel.TaskDetailsViewModel;
import Shared.Model.Task;

import javax.swing.*;

public class TaskDetails extends JDialog
{
    private JPanel contentPane;
    private JTextField titleTextField;
    private JTextField providerTextField;
    private JButton downloadButton;
    private JButton closeButton;
    private JTextArea detailsTextArea;
    private JComboBox stateComboBox;
    private JButton editButton;
    private JButton saveButton;
    private JTextField receiverTextField;
    private final Task selectedTask;
    private final MainWindow owner;


    public TaskDetails(MainWindow owner, Task selectedTask)
    {
        super(owner);
        this.selectedTask = selectedTask;
        this.owner = owner;
        new TaskDetailsViewModel(this);

        initialize();
    }

    public JTextField getTitleTextField()
    {
        return titleTextField;
    }

    public JTextArea getDetailsTextArea()
    {
        return detailsTextArea;
    }

    public JTextField getProviderTextField()
    {
        return providerTextField;
    }

    public JButton getDownloadButton()
    {
        return downloadButton;
    }

    public JButton getCloseButton()
    {
        return closeButton;
    }

    public Task getSelectedTask()
    {
        return selectedTask;
    }

    public JComboBox getStateComboBox()
    {
        return stateComboBox;
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getEditButton()
    {
        return editButton;
    }

    public JTextField getReceiverTextField()
    {
        return receiverTextField;
    }

    @Override
    public MainWindow getOwner()
    {
        return owner;
    }

    public void initialize()
    {
        setContentPane(contentPane);
        setModal(true);
        setSize(600,400);
        setResizable(false);

        getTitleTextField().setText(selectedTask.getTitle());
        getDetailsTextArea().setText(selectedTask.getDescription());
        getProviderTextField().setText(selectedTask.getCreator());
        getReceiverTextField().setText(selectedTask.getReceiver());
        getStateComboBox().setSelectedItem(selectedTask.getStatus());
        getDownloadButton().setText(String.valueOf(selectedTask.getFileAmount()));

        setVisible(true);
    }
}


