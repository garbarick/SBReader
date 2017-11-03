package ru.net.serbis.reader;

/**
 * SEBY0408
 */
public class Log
{
    public static void info(Class clazz, String message)
    {
        android.util.Log.i(clazz.getName(), message);
    }

    public static void info(Class clazz, String message, Throwable e)
    {
        android.util.Log.i(clazz.getName(), message, e);
    }
	
	public static void info(Class clazz, Throwable e)
    {
        android.util.Log.i(clazz.getName(), "Error", e);
    }
	
	public static void info(Object object, String message)
    {
        info(object.getClass(), message);
    }

    public static void info(Object object, String message, Throwable e)
    {
        info(object.getClass(), message, e);
    }

	public static void info(Object object, Throwable e)
    {
        info(object.getClass(), "Error", e);
    }
}
