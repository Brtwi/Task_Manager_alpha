package Client.Model;

import Shared.Model.Task;

import javax.swing.*;
import java.awt.*;

public class TaskRenderList extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        if (value instanceof Task task)
        {
            String displayText = task.getTitle() + " - " + task.getStatus();
            return super.getListCellRendererComponent(
                    list, displayText, index, isSelected, cellHasFocus);
        }

        return super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
    }
}
