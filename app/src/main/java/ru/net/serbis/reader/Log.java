package ru.net.serbis.reader;

/**
 * SEBY0408
 */
public class Log
{
    public static void info(Object object, String message)
    {
        android.util.Log.i(object.getClass().getName(), message);
    }

    public static void info(Object object, String message, Throwable e)
    {
        android.util.Log.i(object.getClass().getName(), message, e);
    }
	
	public static void info(Object object, Throwable e)
    {
        android.util.Log.i(object.getClass().getName(), "Error", e);
    }
}
