package com.bakery.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;

/**
 * AppInitializer — initialises DAOFactory on application startup.
 * Sets dataDir in ServletContext so all servlets can access it.
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String realPath = sce.getServletContext().getRealPath("/WEB-INF/data");
        File dir = new File(realPath);
        if (!dir.exists()) dir.mkdirs();

        sce.getServletContext().setAttribute("dataDir", realPath);
        DAOFactory.getInstance(realPath);   // warm up all DAOs / seed files
        System.out.println("[AppInitializer] BakeryPlatform started. Data dir: " + realPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[AppInitializer] BakeryPlatform shutting down.");
    }
}
