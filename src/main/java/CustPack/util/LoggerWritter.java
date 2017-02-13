package CustPack.util;

import org.slf4j.LoggerFactory;

/**
 * Handle for the logging resource (slf4j) and the displayed syntax
 */

public class LoggerWritter {
    public static void Write(final Class className, final String methodName, final String deatils)
    {
        LoggerFactory.getLogger(className).info(String.format("%s - %s", methodName, deatils));
    }

    public static void WriteWithId(final Class className, final String methodName,final String deatils, final String id)
    {
        LoggerFactory.getLogger(className).info(String.format("%s - %s, with id %s", methodName, deatils, id));
    }

    public static void WriteError(final Class className, final String title, final String deatils)
    {
        LoggerFactory.getLogger(className).error(title, deatils);
    }
}
