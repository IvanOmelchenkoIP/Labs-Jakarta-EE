package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import controller.WebConstants;
import service.InMemoryVotingService;
import service.VotingService;

@WebListener
public class VotingContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        VotingService service = new InMemoryVotingService();
        sce.getServletContext().setAttribute(WebConstants.ATTR_VOTING_SERVICE, service);
    }
}
