    public void addReport() {
        combinereport.condition.typereport.Schedule autoupdate = new combinereport.condition.typereport.Schedule();
        autoupdate.setData("", "", database, Reportmap);
        String path = autoupdate.getPath(reportname);
        File file = new File(path);
        if (!file.exists() || file.length() == 0) process = "mod";
        try {
            PeriodDate perioddate = new PeriodDate(database);
            SimpleDateFormat sdm = new SimpleDateFormat(this.pattern);
            String date = sdm.format(new Date());
            Date d = sdm.parse(date);
            date = sdm.format(d);
            period = gid.getId(scheduletable.get(period).toString());
            if (scheduletable.containsKey(startdate)) {
                startdate = scheduletable.get(startdate).toString();
                currentdate = scheduletable.get(currentdate).toString();
                String nextstartdate = perioddate.getNextDate(period, startdate);
                int i = perioddate.compareDate(currentdate, nextstartdate);
                int j = perioddate.compareDate(date, startdate);
                currentdate = "currentdate";
                if (i > 0 || j > 0) {
                    String nextdate;
                    String user = "systemuser";
                    Uservalidation uservalidation = new Uservalidation(database);
                    String password = uservalidation.getPasword(user);
                    MessageGenerator mg = new MessageGenerator(database);
                    Hashtable<Object, Object> table = new Hashtable<Object, Object>();
                    table.put("startdate", startdate.trim());
                    String addrequest = mg.messagegerat("property_details", table, "add");
                    table.put("startdate", nextstartdate.trim());
                    String modrequest = mg.messagegerat("property_details", table, "mod");
                    String request = "11 property_details mod*" + gid.getItem(object) + modrequest + addrequest + "#" + password;
                    Control control = new Control(user, request, database);
                    control.messageProcessing();
                    control.requestProcess();
                }
            }
            if (scheduletable.containsKey(enddate)) {
                enddate = scheduletable.get(enddate).toString();
                int i = perioddate.compareDate(enddate, date);
                if (i < 0) {
                    Reportmap.clear();
                    scheduletable.clear();
                    database.getUpdate("update report_master set status=5 where tid='" + value + "' and report_name='" + reportname + "'");
                }
            }
            if (scheduletable.containsKey(currentdate)) {
                currentdate = scheduletable.get(currentdate).toString();
                String nextdate = perioddate.getNextDate(period, startdate);
                int i = perioddate.compareDate(nextdate, currentdate);
                if (i > 0) {
                    String user = "systemuser";
                    Uservalidation uservalidation = new Uservalidation(database);
                    String password = uservalidation.getPasword(user);
                    MessageGenerator mg = new MessageGenerator(database);
                    Hashtable<Object, Object> table = new Hashtable<Object, Object>();
                    table.put("currentdate", currentdate);
                    String addrequest = mg.messagegerat("property_details", table, "add");
                    table.put("currentdate", nextdate);
                    String modrequest = mg.messagegerat("property_details", table, "mod");
                    String request = "11 property_details mod*" + gid.getItem(object) + modrequest + addrequest + "#" + password;
                    Control control = new Control(user, request, database);
                    control.messageProcessing();
                    control.requestProcess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
