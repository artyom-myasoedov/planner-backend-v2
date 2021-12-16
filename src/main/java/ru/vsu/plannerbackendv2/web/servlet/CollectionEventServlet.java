package ru.vsu.plannerbackendv2.web.servlet;

import ru.vsu.plannerbackendv2.dao.entity.Event;
import ru.vsu.plannerbackendv2.dao.entity.EventType;
import ru.vsu.plannerbackendv2.di.Application;
import ru.vsu.plannerbackendv2.service.EventService;
import ru.vsu.plannerbackendv2.service.TimeComparisonOperation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@WebServlet(name = "CollectionEventServlet", value = "/event/*")
public class CollectionEventServlet extends HttpServlet {

    private EventService service;
    private Map<String, BiFunction<String, String, Collection<Event>>> criterias2Actions;
    private final Map<String, String> criteria2ParamName = new HashMap<>() {{
        put("byYear", "year");
        put("byMonth", "month");
        put("byName", "name");
    }};

    @Override
    public void init() throws ServletException {
        super.init();
        service = Application.getContext().getBean(EventService.class);
        criterias2Actions = initCriterias2Actions();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] urlParts = request.getRequestURI().split("/");
        if (urlParts.length >= 2 && "event".equals(urlParts[urlParts.length - 2])
                && (urlParts[urlParts.length - 1].equals("byDate")
                || criterias2Actions.containsKey(urlParts[urlParts.length - 1]))) {
            Collection<Event> res;
            String criteriaName = urlParts[urlParts.length - 1];
            try {
                if ("byDate".equals(criteriaName)) {
                    res = findByDate(request);
                } else {
                    res = criterias2Actions.get(criteriaName)
                            .apply(request.getParameter(criteria2ParamName.get(criteriaName)),
                                    request.getParameter("types"));
                }
                request.setAttribute("res", res);
                request.getRequestDispatcher("/collectionPage.jsp").forward(request, response);
            } catch (Exception e) {
                handleException(request, response, e);
            }
        } else {
            handleException(request, response, new IllegalArgumentException("Неправильный URI: " + request.getRequestURI()));
        }

    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        request.setAttribute("error", e);
        request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
    }

    private Collection<Event> findByDate(HttpServletRequest request) {
        String date = request.getParameter("date");
        LocalDateTime localDateTime = LocalDateTime.parse(date + "T00:00");
        String types = request.getParameter("types") == null ? "ANY" : request.getParameter("types");
        return service.findByDate(localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                EventType.toList(EventType.valueOf(types)),
                TimeComparisonOperation.valueOf(request.getParameter("operation")));
    }

    private Map<String, BiFunction<String, String, Collection<Event>>> initCriterias2Actions() {
        Map<String, BiFunction<String, String, Collection<Event>>> res = new HashMap<>();
        res.put("byTypes", (nothing, types) -> service.findByTypes(EventType.toList(EventType.valueOf(types))));
        res.put("byYear", (year, types) -> service.findByYear(Integer.parseInt(year), EventType.toList(EventType.valueOf(types))));
        res.put("byMonth", (month, types) -> service.findByMonth(Integer.parseInt(month), EventType.toList(EventType.valueOf(types))));
        res.put("byName", (name, types) -> service.findByNameLike(name, EventType.toList(EventType.valueOf((types == null) ? "ANY" : types))));
        return res;
    }
}
