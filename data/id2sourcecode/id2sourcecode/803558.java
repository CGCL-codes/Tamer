    public static void doDailyTimeTransmit(String host, String database, String dbuser, String password, String efrom, String eserver, String euser, String epasswd, String sendto) throws Exception {
        Connection con;
        System.out.println("Daily Callslip Report\n\n");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "", dbuser, password);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        Statement stmt2 = con.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select department, transmit from tech_table where transmit=1 group by department;");
        Format formatter;
        Calendar now = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String s = formatter.format(date);
        String timesheetdate = formatter.format(date);
        Date expireationDate = null;
        expireationDate = dateFormatter.parse(s);
        Calendar dateToBeTested = Calendar.getInstance();
        dateToBeTested.setTime(expireationDate);
        dateToBeTested.add(Calendar.DAY_OF_YEAR, -1);
        timesheetdate = doFormatDate(dateToBeTested.getTime());
        System.out.println("new Time Sheet Date: " + timesheetdate + "\n\n");
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
            v = TimeSheetSummary.getLoginItemsSingleDate(con, lusername, doFormatDateDb(getDateDb(timesheetdate)));
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
                mbody = combinestring(mbody, "</table><br>");
            } else {
                mbody = combinestring(mbody, "<table border=0 width=\"75%\" align=\"left\">");
                mbody = combinestring(mbody, "<tr><td>**NO DATA TRANSMITTED</td></tr></table>");
            }
            mbody = combinestring(mbody, "<table border=0 width=\"75%\" align=\"left\">");
            mbody = combinestring(mbody, "<tr><td> </td></tr></table><br>");
        }
        mbody2 = combinestring(mbody2, "<html><basefont size=-1>");
        mbody2 = combinestring(mbody2, "<head><title>Time Sheet Report</title></head><body><h2>By Department</h2>");
        while (rs2.next()) {
            String sdepartment = rs2.getString("department");
            Vector v;
            v = TimeSheetSummary.getDepartmentItemsSingleDate(con, sdepartment, doFormatDateDb(getDateDb(timesheetdate)));
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
                mbody2 = combinestring(mbody2, "</table><br>");
            } else {
                mbody2 = combinestring(mbody2, "<table border=0 width=\"75%\" align=\"left\">");
                mbody2 = combinestring(mbody2, "<tr><td>**NO DATA TRANSMITTED</td></tr></table>");
            }
            mbody2 = combinestring(mbody2, "<table border=0 width=\"75%\" align=\"left\">");
            mbody2 = combinestring(mbody2, "<tr><td> </td></tr></table><br>");
        }
        mbody = "";
    }
