package vn.rikkei.service;

import vn.rikkei.dao.StatisticDAO;

import java.util.List;

public class StatisticService {

    private StatisticDAO dao = new StatisticDAO();

    public List<String> getTop5Products() {
        return dao.getTop5Products();
    }
}