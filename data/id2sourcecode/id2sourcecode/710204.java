    public static void doWeeklyTimeTransmit(String host, String database, String dbuser, String password, String efrom, String eserver, String euser, String epasswd, String sendto) throws Exception {
        Connection con;
        System.out.println("Weekly Time Report\n");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "", dbuser, password);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        Statement stmt2 = con.createStatement();
        Statement stmt = con.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select department, transmit from tech_table where transmit=1 group by department;");
        Format formatter;
        Calendar now = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String s = formatter.format(date);
        Date expireationDate = null;
        expireationDate = dateFormatter.parse(s);
        Calendar dateToBeTested = Calendar.getInstance();
        dateToBeTested.setTime(expireationDate);
        dateToBeTested.add(Calendar.DAY_OF_YEAR, -1);
        String enddate = doFormatDate(dateToBeTested.getTime());
        dateToBeTested.add(Calendar.DAY_OF_YEAR, -8);
        String startdate = doFormatDate(dateToBeTested.getTime());
        System.out.println("Start Date: " + startdate + "\n");
        System.out.println("End Date: " + enddate + "\n");
        Vector u;
        String mbody = "";
        String mbody2 = "";
        mbody = combinestring(mbody, "<html><basefont size=-1>");
        mbody = combinestring(mbody, "<head><title>Time Sheet Report</title></head><body><h2>By Technician</h2>");
        u = UniTechInfo.getAllTransmitItems(con);
        int counter = 0;
        for (int iu = 0; iu < u.size(); iu++) {
            UniTechInfo tu = (UniTechInfo) u.elementAt(iu);
            String tech_init = tu.getTechInit();
            String lusername = tu.getUserName();
            String tech_name = tu.getTechName();
            String department = tu.getDepartment();
            Vector v;
            v = TimeSheetSummary.getLoginItemsMultiDate(con, lusername, doFormatDateDb(getDateDb(startdate)), doFormatDateDb(getDateDb(enddate)));
            mbody = combinestring(mbody, "<table border=1 width=\"75%\" align=\"left\">");
            mbody = combinestring(mbody, "<tr><td>Tech ID: " + lusername + " - " + tech_name + "</td></tr>");
            mbody = combinestring(mbody, "</table>");
            if (v.size() > 0) {
                mbody = combinestring(mbody, "<table border=1 width=\"75%\" align=\"left\">");
                mbody = combinestring(mbody, "<tr><td>Call-Type</td><td>Count</td><td>Total-Collected</td><td>Non-Commision-Billed</td><td>Commision-Billed</td><td>Commision</td><td>Time</td><td>Time-Without-Travel</td></tr>");
                for (int i = 0; i < v.size(); i++) {
                    TimeSheetSummary ts = (TimeSheetSummary) v.elementAt(i);
                    String tamount = ts.Amount();
                    String tcamount = ts.CAmount();
                    String tamount_collected = ts.AmountCollected();
                    String tcommision = ts.Commision();
                    String ctype = ts.CType();
                    String callcount = ts.CallCount();
                    String timewithtravel = ts.TimeWithTravel();
                    String timenotravel = ts.TimeNoTravel();
                    mbody = combinestring(mbody, "<tr><td>" + ctype + "</td><td>" + callcount + "</td><td>" + tamount_collected + "</td><td>" + tamount + "</td><td>" + tcamount + "</td><td>" + tcommision + "</td><td>" + timewithtravel + "</td><td>" + timenotravel + "</td></tr>");
                }
                ResultSet rs = stmt.executeQuery("select count(tsid) as callcount, sum(amount) as amount, sum(amount_collected) as amount_collected, sum(camount) as camount, sum(commision) as commision,  ucase(SEC_TO_TIME(sum(TIME_TO_SEC(subtime(time_out,dispatch_time))))) as time_with_travel,  ucase(SEC_TO_TIME(sum(TIME_TO_SEC(subtime(time_out,time_in))))) as time_no_travel from time_sheet where login='" + lusername + "' and tdate>='" + doFormatDateDb(getDateDb(startdate)) + "' and tdate<='" + doFormatDateDb(getDateDb(enddate)) + "';");
                while (rs.next()) {
                    String tamount = rs.getString("amount");
                    String tcamount = rs.getString("camount");
                    String tamount_collected = rs.getString("amount_collected");
                    String tcommision = rs.getString("commision");
                    String callcount = rs.getString("callcount");
                    String timewithtravel = rs.getString("time_with_travel");
                    String timenotravel = rs.getString("time_no_travel");
                    mbody = combinestring(mbody, "<tr><td>Total</td><td>" + callcount + "</td><td>" + tamount_collected + "</td><td>" + tamount + "</td><td>" + tcamount + "</td><td>" + tcommision + "</td><td>" + timewithtravel + "</td><td>" + timenotravel + "</td></tr>");
                }
                mbody = combinestring(mbody, "</table><br>");
            } else {
                mbody = combinestring(mbody, "<table border=0 width=\"75%\" align=\"left\">");
                mbody = combinestring(mbody, "<tr><td>**NO DATA TRANSMITTED</td></tr></table><br>");
            }
            mbody = combinestring(mbody, "<table border=0 width=\"75%\" align=\"left\">");
            mbody = combinestring(mbody, "<tr><td> </td></tr></table><br>");
        }
        mbody2 = combinestring(mbody2, "<html><basefont size=-1>");
        mbody2 = combinestring(mbody2, "<head><title>Time Sheet Report</title></head><body><h2>By Department</h2>");
        while (rs2.next()) {
            String sdepartment = rs2.getString("department");
            Vector v;
            v = TimeSheetSummary.getDepartmentItemsMultiDate(con, sdepartment, doFormatDateDb(getDateDb(startdate)), doFormatDateDb(getDateDb(enddate)));
            mbody2 = combinestring(mbody2, "<table border=1 width=\"75%\" align=\"left\">");
            mbody2 = combinestring(mbody2, "<tr><td>Department:  " + sdepartment + "</td></tr>");
            mbody2 = combinestring(mbody2, "</table>");
            if (v.size() > 0) {
                mbody2 = combinestring(mbody2, "<table border=1 width=\"75%\" align=\"left\">");
                mbody2 = combinestring(mbody2, "<tr><td>Call-Type</td><td>Count</td><td>Total-Collected</td><td>Non-Commision-Billed</td><td>Commision-Billed</td><td>Commision</td><td>Time</td><td>Time-Without-Travel</td></tr>");
                for (int i = 0; i < v.size(); i++) {
                    TimeSheetSummary ts = (TimeSheetSummary) v.elementAt(i);
                    String tamount = ts.Amount();
                    String tcamount = ts.CAmount();
                    String tamount_collected = ts.AmountCollected();
                    String tcommision = ts.Commision();
                    String ctype = ts.CType();
                    String callcount = ts.CallCount();
                    String timewithtravel = ts.TimeWithTravel();
                    String timenotravel = ts.TimeNoTravel();
                    mbody2 = combinestring(mbody2, "<tr><td>" + ctype + "</td><td>" + callcount + "</td><td>" + tamount_collected + "</td><td>" + tamount + "</td><td>" + tcamount + "</td><td>" + tcommision + "</td><td>" + timewithtravel + "</td><td>" + timenotravel + "</td></tr>");
                }
                ResultSet rs3 = stmt.executeQuery("select tech_table.department as department, count(tsid) as callcount, sum(amount) as amount, sum(amount_collected) as amount_collected, sum(camount) as camount, sum(commision) as commision,  ucase(SEC_TO_TIME(sum(TIME_TO_SEC(subtime(time_out,dispatch_time))))) as time_with_travel,  ucase(SEC_TO_TIME(sum(TIME_TO_SEC(subtime(time_out,time_in))))) as time_no_travel from time_sheet, tech_table  where time_sheet.login=tech_table.username and tdate>='" + doFormatDateDb(getDateDb(startdate)) + "' and tdate<='" + doFormatDateDb(getDateDb(enddate)) + "' and department='" + sdepartment + "' group by department;");
                while (rs3.next()) {
                    String tamount = rs3.getString("amount");
                    String tcamount = rs3.getString("camount");
                    String tamount_collected = rs3.getString("amount_collected");
                    String tcommision = rs3.getString("commision");
                    String callcount = rs3.getString("callcount");
                    String timewithtravel = rs3.getString("time_with_travel");
                    String timenotravel = rs3.getString("time_no_travel");
                    mbody2 = combinestring(mbody2, "<tr><td>Total</td><td>" + callcount + "</td><td>" + tamount_collected + "</td><td>" + tamount + "</td><td>" + tcamount + "</td><td>" + tcommision + "</td><td>" + timewithtravel + "</td><td>" + timenotravel + "</td></tr>");
                }
                mbody2 = combinestring(mbody2, "</table>");
            } else {
                mbody2 = combinestring(mbody2, "<table border=0 width=\"75%\" align=\"left\">");
                mbody2 = combinestring(mbody2, "<tr><td>**NO DATA TRANSMITTED</td></tr></table>");
            }
            mbody2 = combinestring(mbody2, "<table border=0 width=\"75%\" align=\"left\">");
            mbody2 = combinestring(mbody2, "<tr><td> </td></tr></table><br>");
        }
        mbody = "";
    }
