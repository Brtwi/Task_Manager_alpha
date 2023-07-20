package Shared.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService
{
    private static String filePath;
    public static void createDirectory(String dirName) throws IOException
    {
        String path = System.getProperty("user.home") + "/Downloads" + "/" + dirName;
        Path dirPath = Paths.get(path);
        filePath = path + File.separator;

        if(!Files.exists(dirPath))
            Files.createDirectory(dirPath);
    }

    public static File createFile(String fileName, byte[] fileData)
    {
        try
        {
            File fileToSave = new File(filePath + fileName);
            FileOutputStream fos = new FileOutputStream(fileToSave);

            fos.write(fileData);
            fos.close();
            return fileToSave;

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
