    private byte[] showAddForm(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        int day = -1;
        int month = -1;
        int year = -1;
        try {
            day = Integer.parseInt(urlData.getParameter("day"));
            month = Integer.parseInt(urlData.getParameter("month"));
            year = Integer.parseInt(urlData.getParameter("year"));
        } catch (Exception e) {
        }
        ScheduleItem item = null;
        String id = urlData.getParameter("id");
        if (id != null && id.length() > 0) {
            item = store.getScheduleItem(id);
        }
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
        if (day == -1 || month == -1 || year == -1) {
            Date start = item.getStart();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            day = cal.get(Calendar.DATE);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "item_form", null);
        Element root = doc.getDocumentElement();
        Calendar cal = Calendar.getInstance();
        if (item != null) cal.setTime(item.getStart());
        root.setAttribute("month", new Integer(month).toString());
        root.setAttribute("year", new Integer(year).toString());
        root.setAttribute("date", new Integer(day).toString());
        if (item != null) root.setAttribute("id", id);
        if (index > -1) root.setAttribute("index", new Integer(index).toString()); else root.setAttribute("index", "");
        Element formEl = doc.createElement("startTimeMin");
        formEl.setAttribute("Name", "Minute");
        formEl.setAttribute("max", "59");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "5");
        formEl.setAttribute("value", new Integer(cal.get(Calendar.MINUTE)).toString());
        root.appendChild(formEl);
        formEl = doc.createElement("startTimeHour");
        formEl.setAttribute("Name", "Hour");
        formEl.setAttribute("max", "23");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "1");
        formEl.setAttribute("value", new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString());
        root.appendChild(formEl);
        formEl = doc.createElement("duration");
        formEl.setAttribute("Name", "Duration");
        formEl.setAttribute("max", "400");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "5");
        if (item != null) formEl.setAttribute("value", new Integer(item.getDuration()).toString()); else formEl.setAttribute("value", "5");
        root.appendChild(formEl);
        Text text = null;
        formEl = doc.createElement("name");
        if (item != null) text = doc.createTextNode(item.getName()); else text = doc.createTextNode("");
        formEl.appendChild(text);
        root.appendChild(formEl);
        formEl = doc.createElement("channel");
        getChannelList(doc, formEl, null);
        if (item != null) formEl.setAttribute("value", item.getChannel()); else formEl.setAttribute("value", "");
        root.appendChild(formEl);
        formEl = doc.createElement("type");
        formEl.setAttribute("Name", "Schedule Type");
        formEl.setAttribute("max", "10");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "1");
        if (item != null) formEl.setAttribute("value", new Integer(item.getType()).toString()); else formEl.setAttribute("value", "0");
        root.appendChild(formEl);
        formEl = doc.createElement("captureType");
        getCaptureTypes(doc, formEl);
        if (item != null) {
            formEl.setAttribute("value", new Integer(item.getCapType()).toString());
        } else {
            formEl.setAttribute("value", "-1");
        }
        root.appendChild(formEl);
        formEl = doc.createElement("pattern");
        getPatternList(doc, formEl);
        if (item != null) formEl.setAttribute("value", item.getFilePattern()); else formEl.setAttribute("value", "");
        root.appendChild(formEl);
        formEl = doc.createElement("capturePath");
        getCapturePaths(doc, formEl);
        if (item != null) {
            formEl.setAttribute("value", new Integer(item.getCapturePathIndex()).toString());
        } else {
            formEl.setAttribute("value", "-1");
        }
        root.appendChild(formEl);
        formEl = doc.createElement("autoDel");
        formEl.setAttribute("Name", "Auto Delete");
        formEl.setAttribute("max", "1");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "1");
        if (item != null) {
            if (item.isAutoDeletable()) formEl.setAttribute("value", "1"); else formEl.setAttribute("value", "0");
        } else formEl.setAttribute("value", "0");
        root.appendChild(formEl);
        formEl = doc.createElement("keepfor");
        formEl.setAttribute("Name", "keep For");
        formEl.setAttribute("max", "120");
        formEl.setAttribute("min", "1");
        formEl.setAttribute("amount", "1");
        if (item != null) formEl.setAttribute("value", new Integer(item.getKeepFor()).toString()); else {
            String keep = store.getProperty("autodel.keepfor");
            formEl.setAttribute("value", keep);
        }
        root.appendChild(formEl);
        formEl = doc.createElement("posttask");
        getTaskList(doc, formEl);
        if (item != null) {
            formEl.setAttribute("value", item.getPostTask());
        } else {
            String defTask = store.getProperty("tasks.deftask");
            formEl.setAttribute("value", defTask);
        }
        root.appendChild(formEl);
        XSL transformer = new XSL(doc, "kb-details.xsl", urlData, headers);
        return transformer.doTransform();
    }
