package controller.voting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import controller.WebConstants;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Voting;
import service.VotingService;

@WebServlet("/voting")
public class VotingListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private VotingService service;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Voting> sorted = new ArrayList<>(service.findAll());
        sorted.sort(Comparator.comparingLong(Voting::getId));
        request.setAttribute("votings", sorted);
        request.getRequestDispatcher(WebConstants.VIEW_LIST).forward(request, response);
    }
}
