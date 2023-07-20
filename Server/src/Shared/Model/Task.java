package Shared.Model;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Task implements Serializable
{
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;
    private String title;
    private String description;
    private final String creator;
    private String receiver;
    private String status;
    private final List<File> files;
    private Integer fileAmount;
    private final String id;

    public Task(String title, String description, String creator, String status, List<File> files, Integer fileAmount)
    {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.status = status;
        this.files = files;
        this.fileAmount = fileAmount;
        id = this.hashCode() + creator;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCreator()
    {
        return creator;
    }

    public String getStatus()
    {
        return status;
    }

    public List<File> getFiles()
    {
        return files;
    }

    public Integer getFileAmount()
    {
        return fileAmount;
    }

    public String getID()
    {
        return id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setFileAmount(Integer fileAmount)
    {
        this.fileAmount = fileAmount;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }
}
