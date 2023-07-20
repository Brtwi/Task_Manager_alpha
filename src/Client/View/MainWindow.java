package Client.View;

import Client.Model.TaskRenderList;
import Client.ViewModel.MainWindowViewModel;
import Shared.Model.Task;

import javax.swing.*;

public class MainWindow extends JFrame
{
    private JPanel mainPanel;
    private JTabbedPane taskPanel;
    private JList<Task> allTaskList;
    private JList<Task> myTaskList;
    private JList<Task> sentTasksList;
    private JButton addTaskButton;
    private JButton deleteTaskButton;
    private final MainWindowViewModel viewModel;

    public MainWindow()
    {
        super("TaskManager");
        viewModel = new MainWindowViewModel(this);
        allTaskList.setCellRenderer(new TaskRenderList());
        myTaskList.setCellRenderer(new TaskRenderList());
        sentTasksList.setCellRenderer(new TaskRenderList());

        initialize();
    }

    public JList getAllTaskList()
    {
        return allTaskList;
    }

    public JList getMyTaskList()
    {
        return myTaskList;
    }

    public JList getOtherTasksList()
    {
        return sentTasksList;
    }

    public JButton getAddTaskButton()
    {
        return addTaskButton;
    }

    public JButton getDeleteTaskButton()
    {
        return deleteTaskButton;
    }

    public MainWindowViewModel getViewModel()
    {
        return viewModel;
    }


    public void initialize()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(650, 500);
        setResizable(false);

        setVisible(true);
    }
}
