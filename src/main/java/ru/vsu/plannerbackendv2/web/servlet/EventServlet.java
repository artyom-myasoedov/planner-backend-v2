package ru.vsu.plannerbackendv2.web.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vsu.plannerbackendv2.dao.entity.Birthday;
import ru.vsu.plannerbackendv2.dao.entity.Event;
import ru.vsu.plannerbackendv2.dao.entity.Meeting;
import ru.vsu.plannerbackendv2.di.Application;
import ru.vsu.plannerbackendv2.service.EventService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "EventServlet", urlPatterns = "/event")
public class EventServlet extends HttpServlet {

    private EventService service;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        service = Application.getContext().getBean(EventService.class);
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Collection<Event> res = Collections.singletonList(service.findById(id));
            req.setAttribute("res", res);
            req.getRequestDispatcher("/collectionPage.jsp").forward(req, resp);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().readLine();
        try {
            Event event = mapEvent(json);
            service.create(event);
            resp.setStatus(200);
            resp.getWriter().println("Создание прошло успешно");
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().readLine();
        Event event;
        try {
            event = mapEvent(json);
            service.update(event);
            resp.setStatus(200);
            resp.getWriter().println("Обновление прошло успешно");
            resp.sendRedirect("/collectionPage.jsp");
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            service.deleteById(id);
            Collection<Event> res = service.findAll();
            req.setAttribute("res", res);
            req.getRequestDispatcher("/collectionPage.jsp").forward(req, resp);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        request.setAttribute("error", e);
        request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
    }


    private Event mapEvent(String json) throws IOException {
        JsonNode node = mapper.readTree(json);
        Event event;
        if (json.contains("BIRTHDAY")) {
            Birthday birthday = new Birthday();
            birthday.setPresent(node.get("present").asText());
            event = birthday;
        } else {
            event = new Meeting();
        }
        event.setDescription(node.get("description").asText());
        event.setName(node.get("name").asText());
        event.setId(node.get("id").asInt());
        event.setDateTime(LocalDateTime.parse(node.get("dateTime").asText()));
        return event;
    }
}
