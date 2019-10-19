package com.touchsoft.java7.spring.config;

import com.touchsoft.java7.core.UserList.UserList;
import com.touchsoft.java7.socket.RegSocketConnectionThread;
import com.touchsoft.java7.web.ChatEndpoints;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.util.logging.LogManager;


public class AppInitializer implements WebApplicationInitializer {

        // Указываем имя нашему Servlet Dispatcher для мапинга
        private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

            // Регистрируем в контексте конфигурационный класс, который мы создадим ниже
            ctx.register(ChatConfiguration.class);
            //ctx.register(ChatEndpoints.class);
            servletContext.addListener(new ContextLoaderListener(ctx));
            ctx.setServletContext(servletContext);

            ServletRegistration.Dynamic servlet = servletContext.addServlet(DISPATCHER_SERVLET_NAME,
                    new DispatcherServlet(ctx));
            servlet.addMapping("/");
            servlet.setLoadOnStartup(1);

            //ServletContext context = servletContextEvent.getServletContext();
            servletContext.setInitParameter("log4j-config-location", "WEB-INF/log4j.properties");
            String log4jConfigFile = servletContext.getInitParameter("log4j-config-location");
            PropertyConfigurator.configure(servletContext.getRealPath("") + File.separator + log4jConfigFile);
            new UserList();
            new RegSocketConnectionThread();


        }

    }