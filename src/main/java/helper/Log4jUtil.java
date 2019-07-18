package helper;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtil {
    private static Log4jUtil log4jUtil;
    private Logger logger;

    public Log4jUtil(String configPath) {
        PropertyConfigurator.configure(configPath);
        this.logger = Logger.getLogger(Log4jUtil.class);
    }

    public static Log4jUtil getLog4jUtil(String configPath) {
        if (log4jUtil == null) {
            log4jUtil = new Log4jUtil(configPath);
        }

        return log4jUtil;
    }

    public static void debug(String str) {
        log4jUtil.logger.debug(str);
    }

    public static void debug(String str, Exception e) {
        log4jUtil.logger.debug(str, e);
    }

    public static void info(String str) {
        log4jUtil.logger.info(str);
    }

    public static void info(String str, Exception e) {
        log4jUtil.logger.info(str, e);
    }

    public static void warn(String str) {
        log4jUtil.logger.warn(str);
    }

    public static void warn(String str, Exception e) {
        log4jUtil.logger.warn(str, e);
    }

    public static void error(String str) {
        log4jUtil.logger.error(str);
    }

    public static void error(String str, Exception e) {
        log4jUtil.logger.error(str, e);
    }

    public static void fatal(String str) {
        log4jUtil.logger.fatal(str);
    }

    public static void fatal(String str, Exception e) {
        log4jUtil.logger.fatal(str, e);
    }
}
