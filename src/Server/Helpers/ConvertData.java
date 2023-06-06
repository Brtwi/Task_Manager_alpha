package Server.Helpers;

import Server.Model.Client;

import java.util.HashSet;
import java.util.Vector;

public class ConvertData
{
    public static Vector<String> convertToVector(HashSet<Client> hashSet)
    {
        Vector<String> vector = new Vector<>();
        for(Client c : hashSet)
        {
            vector.add(c.getUsername());
        }
        return vector;
    }
}
