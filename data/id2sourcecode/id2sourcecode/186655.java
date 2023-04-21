    private byte[] showCalendar(HTTPurl urlData, HashMap headers) throws Exception {
        String id = urlData.getParameter("id");
        ScheduleItem item = store.getScheduleItem(id);
        int index = -1;
        try {
            index = Integer.parseInt(urlData.getParameter("index"));
        } catch (Exception e) {
        }
        if (item != null && (item.getState() != ScheduleItem.FINISHED && item.getState() != ScheduleItem.WAITING && item.getState() != ScheduleItem.SKIPPED && item.getState() != ScheduleItem.ERROR)) {
            StringBuffer out = new StringBuffer();
            out.append("HTTP/1.0 302 Moved Temporarily\n");
            out.append("Location: /servlet/KBScheduleDataRes\n\n");
            return out.toString().getBytes();
        }
        int month = -1;
        int year = -1;
        int day = -1;
        try {
            month = Integer.parseInt(urlData.getParameter("month"));
            year = Integer.parseInt(urlData.getParameter("year"));
        } catch (Exception e) {
        }
        Calendar cal = Calendar.getInstance();
        if (item != null) cal.setTime(item.getStart());
        day = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE, 1);
        if (month > -1 && year > -1) {
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
        }
        if (day > cal.getActualMaximum(Calendar.DATE)) day = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DATE, day);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "cal", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("selectedDay", new Integer(day).toString());
        root.setAttribute("description", store.monthNameFull.get(new Integer(month)) + " " + year);
        cal.add(Calendar.MONTH, -1);
        root.setAttribute("prevMonth", new Integer(cal.get(Calendar.MONTH)).toString());
        root.setAttribute("prevYear", new Integer(cal.get(Calendar.YEAR)).toString());
        cal.add(Calendar.MONTH, 2);
        root.setAttribute("nextMonth", new Integer(cal.get(Calendar.MONTH)).toString());
        root.setAttribute("nextYear", new Integer(cal.get(Calendar.YEAR)).toString());
        cal.add(Calendar.MONTH, -1);
        if (item != null) root.setAttribute("itemID", item.toString()); else root.setAttribute("itemID", "");
        if (index > -1) root.setAttribute("index", new Integer(index).toString()); else root.setAttribute("index", "");
        int currentMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.DATE, 1);
        int dayNameStart = cal.getFirstDayOfWeek();
        for (int x = 0; x < 7; x++) {
            String dayName = (String) store.dayName.get(new Integer(dayNameStart));
            if (dayName == null) dayName = "NULL";
            Element dayEl = doc.createElement("dayNames");
            Element dayUrl = doc.createElement("name");
            Text dayUrlTest = doc.createTextNode(dayName);
            dayUrl.appendChild(dayUrlTest);
            dayEl.appendChild(dayUrl);
            root.appendChild(dayEl);
            dayNameStart++;
            if (dayNameStart > cal.getActualMaximum(Calendar.DAY_OF_WEEK)) dayNameStart = cal.getActualMinimum((Calendar.DAY_OF_WEEK));
        }
        for (int x = cal.getFirstDayOfWeek(); x < cal.get(Calendar.DAY_OF_WEEK); x++) {
            Element dayEl = doc.createElement("day");
            dayEl.setAttribute("date", "0");
            dayEl.setAttribute("month", new Integer(cal.get(Calendar.MONTH)).toString());
            dayEl.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
            dayEl.setAttribute("DayOfWeek", new Integer(x).toString());
            dayEl.setAttribute("week", new Integer(cal.get(Calendar.WEEK_OF_MONTH)).toString());
            Element dayUrl = doc.createElement("url");
            Text dayUrlTest = doc.createTextNode("");
            dayUrl.appendChild(dayUrlTest);
            dayEl.appendChild(dayUrl);
            root.appendChild(dayEl);
        }
        while (cal.get(Calendar.MONTH) == currentMonth) {
            Element dayEl = doc.createElement("day");
            dayEl.setAttribute("date", new Integer(cal.get(Calendar.DATE)).toString());
            dayEl.setAttribute("month", new Integer(cal.get(Calendar.MONTH)).toString());
            dayEl.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
            dayEl.setAttribute("DayOfWeek", new Integer(cal.get(Calendar.DAY_OF_WEEK)).toString());
            dayEl.setAttribute("week", new Integer(cal.get(Calendar.WEEK_OF_MONTH)).toString());
            String action = "/servlet/" + urlData.getServletClass() + "?action=02&day=" + cal.get(Calendar.DATE) + "&month=" + cal.get(Calendar.MONTH) + "&year=" + cal.get(Calendar.YEAR);
            if (item != null) action += "&id=" + URLEncoder.encode(item.toString(), "UTF-8");
            if (index > -1) action += "&index=" + index;
            Element dayUrl = doc.createElement("url");
            Text dayUrlTest = doc.createTextNode(action);
            dayUrl.appendChild(dayUrlTest);
            dayEl.appendChild(dayUrl);
            root.appendChild(dayEl);
            cal.add(Calendar.DATE, 1);
        }
        cal.add(Calendar.DATE, -1);
        for (int x = cal.get(Calendar.DAY_OF_WEEK) + 1; x <= 7; x++) {
            Element dayEl = doc.createElement("day");
            dayEl.setAttribute("date", "0");
            dayEl.setAttribute("month", new Integer(cal.get(Calendar.MONTH)).toString());
            dayEl.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
            dayEl.setAttribute("DayOfWeek", new Integer(x).toString());
            dayEl.setAttribute("week", new Integer(cal.get(Calendar.WEEK_OF_MONTH)).toString());
            Element dayUrl = doc.createElement("url");
            Text dayUrlTest = doc.createTextNode("");
            dayUrl.appendChild(dayUrlTest);
            dayEl.appendChild(dayUrl);
            root.appendChild(dayEl);
        }
        XSL transformer = new XSL(doc, "kb-cal.xsl", urlData, headers);
        return transformer.doTransform();
    }
