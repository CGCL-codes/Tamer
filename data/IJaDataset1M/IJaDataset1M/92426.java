package com.etc.bin;

import com.etc.bin.base.HttpBaseServlet;
import com.etc.bin.base.RequestManager;
import com.etc.bin.base.SessionManager;
import com.etc.bin.beans.UserLoginBeans;
import com.etc.db.oracle.Connector;
import com.etc.output.OutputManager;
import com.etc.ui.OptionManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.sql.ResultSet;
import java.math.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author MaGicBank
 * @version
 */
public class LoadVars extends HttpBaseServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestManager req = new RequestManager(request);
        SessionManager session = new SessionManager(request);
        UserLoginBeans user = (UserLoginBeans) session.get("#USER#");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        if (req.command("PARTNER") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME FROM PARTNER WHERE CODE LIKE '" + req.getString("code") + "'");
                if (raw.next()) {
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("USER") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME FROM USER_INFO WHERE CODE LIKE '" + req.getString("code") + "'");
                if (raw.next()) {
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("UID") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME FROM USER_INFO WHERE ID = " + req.getString("code"));
                if (raw.next()) {
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("PRODUCT") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME, UCODE FROM PRODUCT_INFO_V WHERE CODE LIKE '" + req.getString("code") + "'");
                if (raw.next()) {
                    BigDecimal cost = BigDecimal.ZERO;
                    BigDecimal disc = BigDecimal.ZERO;
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME") + "|" + raw.getString("UCODE") + "|" + cost + "|" + disc, response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("PROCOST") && !req.getString("code").equalsIgnoreCase("") && !req.getString("subunit").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                BigDecimal cost = BigDecimal.ZERO;
                BigDecimal disc = BigDecimal.ZERO;
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT PRICE, DISCOUNT FROM PORDER_LIST_V WHERE ID = (SELECT MAX(ID) FROM PORDER_LIST_V WHERE PROCODE LIKE '" + req.getString("code") + "' AND SUBUNIT = " + req.getString("subunit") + ")");
                if (raw.next()) {
                    cost = raw.getBigDecimal("PRICE");
                    disc = raw.getBigDecimal("DISCOUNT");
                    printText(cost + "|" + disc, response);
                } else {
                    printText(cost + "|" + disc, response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("PROPRICE") && !req.getString("code").equalsIgnoreCase("") && !req.getString("subunit").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT (BALANCE_V.PRICE * SUBPRODUCT_V.QTY) AS PRICE FROM BALANCE_V, SUBPRODUCT_V WHERE BALANCE_V.PROCODE = SUBPRODUCT_V.PROCODE AND BALANCE_V.LOT LIKE '" + req.getString("code") + "' AND SUBPRODUCT_V.ID = " + req.getString("subunit"));
                if (raw.next()) {
                    printText(raw.getString("PRICE"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("PROPRICE2") && !req.getString("id").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT PRICE FROM SUBPRODUCT_V WHERE ID = " + req.getString("id"));
                if (raw.next()) {
                    printText(raw.getString("PRICE"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("DISTCODE") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT PROCODE, PRONAME, LOT, UNIT, AMOUNT_BALANCE, PRICE, DISP_LOT FROM BALANCE_V WHERE AMOUNT_BALANCE > 0 AND PROCODE LIKE '" + req.getString("code") + "' ORDER BY EXP ASC");
                if (raw.next()) {
                    printText(raw.getString("PROCODE") + "|" + raw.getString("PRONAME") + "|" + deNull(raw.getString("LOT")) + "|" + raw.getString("UNIT") + "|" + raw.getString("AMOUNT_BALANCE") + "|" + raw.getString("PRICE") + "|" + raw.getString("DISP_LOT"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("DISTLOT") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT PROCODE, PRONAME, LOT, UNIT, AMOUNT_BALANCE, PRICE, DISP_LOT FROM BALANCE_V WHERE AMOUNT_BALANCE > 0 AND (LOT LIKE '" + req.getString("code") + "' OR DISP_LOT LIKE '" + req.getString("code") + "') ORDER BY EXP ASC");
                if (raw.next()) {
                    printText(raw.getString("PROCODE") + "|" + raw.getString("PRONAME") + "|" + deNull(raw.getString("LOT")) + "|" + raw.getString("UNIT") + "|" + raw.getString("AMOUNT_BALANCE") + "|" + raw.getString("PRICE") + "|" + raw.getString("DISP_LOT"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("PARTDESC") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME, TERMID, TERMNAME, CONTACT FROM PARTNER_INFO_V WHERE CODE LIKE '" + req.getString("code") + "'");
                if (raw.next()) {
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME") + "|" + raw.getString("TERMID") + "|" + raw.getString("TERMNAME") + "|" + deNull(raw.getString("CONTACT")), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("SUBUNIT") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT * FROM SUBPRODUCT_V WHERE PROCODE LIKE '" + req.getString("code") + "' ORDER BY QTY ASC");
                Map<String, String> data = new HashMap<String, String>();
                while (raw.next()) {
                    data.put(raw.getString("UNITNAME"), raw.getString("ID"));
                }
                raw.close();
                stm.close();
                OutputManager.printHtml(OptionManager.buildOption(data), response);
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("SUBUID") && !req.getString("id").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT * FROM SUBPRODUCT_V WHERE ID = " + req.getString("id"));
                HashMap<String, String> data = new HashMap<String, String>();
                while (raw.next()) {
                    data.put(raw.getString("UNITNAME"), raw.getString("ID"));
                }
                raw.close();
                stm.close();
                OutputManager.printHtml(OptionManager.buildOption(data), response);
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("CURRENCY")) {
            Connection conn = Connector.getConnection();
            try {
                String def = req.getString("id").equalsIgnoreCase("") ? "0" : req.getString("id");
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, UNIT FROM EXCHANGE WHERE ID = " + def);
                HashMap<String, String> data = new HashMap<String, String>();
                if (raw.next()) {
                    data.put(currency.get(raw.getString("UNIT")), raw.getString("ID"));
                    OutputManager.printHtml(OptionManager.buildOption(data), response);
                } else {
                    printHtml(getExchangeOption(), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        } else if (req.command("ACCODE") && !req.getString("code").equalsIgnoreCase("")) {
            Connection conn = Connector.getConnection();
            try {
                Statement stm = conn.createStatement();
                ResultSet raw = stm.executeQuery("SELECT ID, CODE, NAME FROM AC_CODE WHERE CODE LIKE '" + req.getString("code") + "'");
                if (raw.next()) {
                    printText(raw.getString("ID") + "|" + raw.getString("CODE") + "|" + raw.getString("NAME"), response);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
        }
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "MINERAL - Load Variable";
    }
}
