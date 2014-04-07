package de.serverfrog.frogchat;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author m-p-h_000
 */
public class SystemConfig {

    static {

        Map<String, String> o = new HashMap<>();
        o.put("log4j.rootLogger", "DEBUG,socket,console");
        o.put("log4j.category.OpenEJB", "info");
        o.put("log4j.category.OpenEJB.options", "info");
        o.put("log4j.category.OpenEJB.server", "info");
        o.put("log4j.category.OpenEJB.startup", "info");
        o.put("log4j.category.OpenEJB.startup.service", "warn");
        o.put("log4j.category.OpenEJB.startup.config", "info");
        o.put("log4j.category.OpenEJB.hsql", "info");
        o.put("log4j.category.CORBA-Adapter", "info");
        o.put("log4j.category.Transaction", "warn");
        o.put("log4j.category.org.apache.activemq", "error");
        o.put("log4j.category.org.apache.geronimo", "error");
        o.put("log4j.category.openjpa", "warn");
//        o.put("log4j.category.org.hibernate", "debug"); 
//        o.put("log4j.category.org.hibernate.internal.SessionFactoryImpl", "error"); 
        o.put("log4j.category.org.hibernate.tool.hbm2ddl", "fatal");
        o.put("log4j.category.de", "trace");

        o.put("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
        o.put("log4j.appender.console.layout", "org.apache.log4j.SimpleLayout");
        o.put("log4j.appender.console.threshold", "ERROR");
        o.put("log4j.appender.socket", "org.apache.log4j.net.SocketAppender");
        o.put("log4j.appender.socket.Port", "4445");
        o.put("log4j.appender.socket.threshold", "TRACE");
        o.put("log4j.appender.socket.RemoteHost", "localhost");
        o.put("log4j.appender.socket.RemoteHost", "localhost");
        o.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");

        OPENEJB_LOG_TESTING = o;
    }

    /**
     * Supplys different logging schema including a socket connection.
     */
    public final static Map<String, String> OPENEJB_LOG_TESTING;
}
