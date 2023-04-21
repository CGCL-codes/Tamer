    private byte[] getScheduleTable(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        int start = 0;
        int show = 10;
        try {
            start = Integer.parseInt(urlData.getParameter("start"));
            show = Integer.parseInt(urlData.getParameter("show"));
        } catch (Exception e) {
        }
        if (start < 0) start = 0;
        int filter = 0;
        try {
            filter = Integer.parseInt(urlData.getParameter("filter"));
        } catch (Exception e) {
        }
        ScheduleItem[] itemsArray = store.getScheduleArray();
        itemsArray = filterItems(itemsArray, (filter != 0));
        Arrays.sort(itemsArray);
        int end = show + start;
        if ((show + start) >= itemsArray.length) end = itemsArray.length;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "schedules", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("start", new Integer(start).toString());
        root.setAttribute("end", new Integer(end).toString());
        root.setAttribute("show", new Integer(show).toString());
        root.setAttribute("total", new Integer(itemsArray.length).toString());
        root.setAttribute("filter", new Integer(filter).toString());
        Element sch = null;
        Element elm = null;
        Text text = null;
        for (int x = start; x < end; x++) {
            ScheduleItem item = itemsArray[x];
            sch = doc.createElement("schedule");
            sch.setAttribute("id", item.toString());
            SimpleDateFormat df = new SimpleDateFormat("E dd MMM h:mm a");
            sch.setAttribute("start", df.format(item.getStart()));
            elm = doc.createElement("schName");
            if (item.getName().length() > 0) text = doc.createTextNode(item.getName()); else text = doc.createTextNode("No Name");
            elm.appendChild(text);
            sch.appendChild(elm);
            elm = doc.createElement("schDur");
            text = doc.createTextNode(new Integer(item.getDuration()).toString());
            elm.appendChild(text);
            sch.appendChild(elm);
            elm = doc.createElement("schChannel");
            text = doc.createTextNode(item.getChannel());
            elm.appendChild(text);
            sch.appendChild(elm);
            elm = doc.createElement("schStatus");
            text = doc.createTextNode(item.getStatus());
            elm.appendChild(text);
            sch.appendChild(elm);
            String type = "";
            if (item.getType() == ScheduleItem.ONCE) type = "Once"; else if (item.getType() == ScheduleItem.DAILY) type = "Daily"; else if (item.getType() == ScheduleItem.WEEKLY) type = "Weekly"; else if (item.getType() == ScheduleItem.MONTHLY) type = "Monthly"; else if (item.getType() == ScheduleItem.WEEKDAY) type = "Week Day"; else if (item.getType() == ScheduleItem.EPG) type = "EPG"; else type = "?" + item.getType() + "?";
            elm = doc.createElement("schType");
            text = doc.createTextNode(type);
            elm.appendChild(text);
            sch.appendChild(elm);
            String action = "/servlet/" + urlData.getServletClass() + "?action=04&id=" + URLEncoder.encode(item.toString(), "UTF-8");
            elm = doc.createElement("action");
            text = doc.createTextNode(action);
            elm.appendChild(text);
            sch.appendChild(elm);
            root.appendChild(sch);
        }
        XSL transformer = new XSL(doc, "kb-schedules.xsl", urlData, headers);
        transformer.addCookie("backURL", urlData.getReqString());
        return transformer.doTransform();
    }
