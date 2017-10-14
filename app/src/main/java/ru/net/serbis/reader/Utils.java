package ru.net.serbis.reader;

import java.io.*;

public class Utils
{
	public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }
}
