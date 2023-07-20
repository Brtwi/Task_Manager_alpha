package Client.View;

import Client.ViewModel.CreateTaskViewModel;

import javax.swing.*;

public class CreateTask extends JDialog
{
    private JPanel contentPane;
    private JTextField titleTextField;
    private JButton addFilesButton;
    private JButton saveButton;
    private JButton sendButton;
    private JButton cancelButton;
    private JLabel amountFileLabel;
    private JTextArea detailsTextArea;
    private JLabel receiverLabel;

    public CreateTask(MainWindow owner)
    {
        super(owner);
        new CreateTaskViewModel(this, owner.getViewModel());

        initialize();
    }

    public JTextField getTitleTextField()
    {
        return titleTextField;
    }

    public JButton getAddFilesButton()
    {
        return addFilesButton;
    }

    public JButton getSaveButton()
    {
        return saveButton;
    }

    public JButton getSendButton()
    {
        return sendButton;
    }

    public JButton getCancelButton()
    {
        return cancelButton;
    }

    public JTextArea getDetailsTextArea()
    {
        return detailsTextArea;
    }

    public JLabel getReceiverLabel()
    {
        return receiverLabel;
    }

    public void initialize()
    {
        setTitle("New task");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setContentPane(contentPane);
        setSize(500,400);
        setResizable(false);
        amountFileLabel.setVisible(false);

        setVisible(true);
    }
}
