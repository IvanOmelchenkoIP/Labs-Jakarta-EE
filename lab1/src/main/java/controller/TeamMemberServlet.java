package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/member-chyrkov",
        "/member-kreslavskyi",
        "/member-kushch",
        "/member-omelchenko"
})
public class TeamMemberServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        String page = switch (path) {
            case "/member-chyrkov" -> "member-chyrkov.jsp";
            case "/member-kreslavskyi" -> "member-kreslavskyi.jsp";
            case "/member-kushch" -> "member-kushch.jsp";
            case "/member-omelchenko" -> "member-omelchenko.jsp";
            default -> throw new ServletException("Невідомий профіль: " + path);
        };
        request.getRequestDispatcher("/" + page).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
