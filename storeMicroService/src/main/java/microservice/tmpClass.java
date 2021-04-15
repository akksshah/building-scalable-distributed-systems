package microservice;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class tmpClass implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Store.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Store.shutdown();
    }
}
