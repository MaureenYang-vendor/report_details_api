package com.api.report.dao.impl;

import com.api.report.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class ReportDaoImpl implements ReportDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Object> getReports() {
        String query = null;

        query = "select * from IRReport_Details";

        List<Object> list = new ArrayList<>();
        try {
            List<Object> finalList = list;
            jdbcTemplate.query(query, (rs, rownumber) -> {
                Map<String, String> map = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i);
                    if (type == Types.VARCHAR) {
                        map.put(rsmd.getColumnLabel(i), rs.getString(i));
                    } else if (type == Types.INTEGER) {
                        map.put(rsmd.getColumnLabel(i), Integer.toString(rs.getInt(i)));
                    }
                }
                finalList.add(map);

                return finalList;
            });
            list = finalList;
        } catch (Exception e) {
            System.out.println("sql exception for query: " + e);
        }

        return list;
    }


    public Map<String, String> getReportById(String reportId) {
        String query = null;
        query = "select * from IRReport_Details WHERE ReportID = '" + reportId + "';";
        System.out.println(query);
        Map<String, String> resultMap = new HashMap<>();
        try {
            Map<String, String> map = new HashMap<>();
            resultMap = jdbcTemplate.queryForObject(query, (rs, rownumber) -> {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i);
                    if (type == Types.VARCHAR) {
                        map.put(rsmd.getColumnLabel(i), rs.getString(i));
                    } else if (type == Types.INTEGER) {
                        map.put(rsmd.getColumnLabel(i), Integer.toString(rs.getInt(i)));
                    }
                }
                return map;
            });
        } catch (Exception e) {
            System.out.println("sql exception for query: " + e);
        }

        return resultMap;
    }
}

