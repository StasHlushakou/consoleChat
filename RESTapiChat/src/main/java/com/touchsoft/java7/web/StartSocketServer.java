package com.touchsoft.java7.web;

import com.touchsoft.java7.socket.RegSocketConnectionThread;
import org.apache.log4j.PropertyConfigurator;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

// Start ConsoleSocket
@WebListener
public class StartSocketServer implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // initialize log4j
        ServletContext context = servletContextEvent.getServletContext();
        String log4jConfigFile = context.getInitParameter("log4j-config-location");
        String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
        PropertyConfigurator.configure(fullPath);
        new RegSocketConnectionThread();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
