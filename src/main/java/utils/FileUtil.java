package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileUtil
{

    public static Properties getProperties(String name)
    {
        Properties pro = new Properties();
        FileInputStream in = null;
        try
        {
            String url = System.getProperty( "user.dir" );
            System.out.println(url);
//            String url = FileUtil.class.getResource(File.separator).getPath();
            in = new FileInputStream(url +File.separator+ name);
            pro.load(in);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                    in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return pro;
    }
}
