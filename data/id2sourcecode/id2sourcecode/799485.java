    private byte[] xmlEPG(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        Calendar start = Calendar.getInstance();
        if (start.get(Calendar.HOUR_OF_DAY) <= 6) {
            start.add(Calendar.DATE, -1);
        }
        int month = start.get(Calendar.MONTH) + 1;
        int day = start.get(Calendar.DATE);
        int year = start.get(Calendar.YEAR);
        try {
            year = Integer.parseInt(urlData.getParameter("year"));
            month = Integer.parseInt(urlData.getParameter("month"));
            day = Integer.parseInt(urlData.getParameter("day"));
        } catch (Exception e) {
        }
        int scrollto = -2;
        try {
            scrollto = Integer.parseInt(urlData.getParameter("scrollto"));
        } catch (Exception e) {
        }
        if (scrollto == -1) {
            scrollto = start.get(Calendar.HOUR_OF_DAY);
        }
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DATE, day);
        start.set(Calendar.HOUR_OF_DAY, 6);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.add(Calendar.SECOND, -1);
        Calendar end = Calendar.getInstance();
        end.setTime(start.getTime());
        end.add(Calendar.HOUR, 24);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("Test", "tv", null);
        GuideStore epgStore = GuideStore.getInstance();
        Vector<String[]> channelMap = epgStore.getChannelMap();
        Set<String> wsChannels = store.getChannels().keySet();
        Element root = doc.getDocumentElement();
        root.setAttribute("date", new Integer(start.get(Calendar.DATE)).toString());
        root.setAttribute("month", (String) store.monthNameShort.get(new Integer(start.get(Calendar.MONTH))));
        root.setAttribute("year", new Integer(start.get(Calendar.YEAR)).toString());
        root.setAttribute("day", (String) store.dayNameFull.get(new Integer(start.get(Calendar.DAY_OF_WEEK))));
        root.setAttribute("scrollto", new Integer(scrollto).toString());
        Calendar now = Calendar.getInstance();
        if (now.after(start) && now.before(end)) {
            Element nowLine = doc.createElement("nowLine");
            nowLine.setAttribute("hour", new Integer(now.get(Calendar.HOUR_OF_DAY)).toString());
            nowLine.setAttribute("minute", new Integer(now.get(Calendar.MINUTE)).toString());
            root.appendChild(nowLine);
        }
        String link = "/servlet/" + urlData.getServletClass() + "?action=12&";
        Vector<String[]> links = epgStore.getEPGlinks(start);
        for (int x = 0; x < links.size(); x++) {
            String[] data = (String[]) links.get(x);
            Element dayEl = doc.createElement("days");
            dayEl.setAttribute("name", data[1]);
            Element dayUrl = doc.createElement("url");
            Text dayUrlTest = doc.createTextNode(link + data[0]);
            dayUrl.appendChild(dayUrlTest);
            dayEl.appendChild(dayUrl);
            root.appendChild(dayEl);
        }
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            Channel wsChannel = store.getChannels().get(map[0]);
            Element channel = doc.createElement("channel");
            channel.setAttribute("id", map[0]);
            if (wsChannel != null) {
                channel.setAttribute("mux_id", wsChannel.getFrequency() + "-" + wsChannel.getBandWidth());
            } else {
                channel.setAttribute("mux_id", "none");
            }
            Element disName = doc.createElement("display-name");
            Text chaName = doc.createTextNode(map[0]);
            disName.appendChild(chaName);
            channel.appendChild(disName);
            root.appendChild(channel);
        }
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss Z");
        StringBuffer dateBuff = new StringBuffer();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.MILLISECOND, 0);
        boolean overlapDetected = false;
        for (int x = 0; x < channelMap.size(); x++) {
            GuideItem prevItem = null;
            String[] map = (String[]) channelMap.get(x);
            String channelName = map[0];
            if (!wsChannels.contains(map[0])) {
                channelName = "Not Mapped";
            } else {
                GuideItem[] programs = epgStore.getPrograms(start.getTime(), end.getTime(), map[1]);
                for (int y = 0; y < programs.length; y++) {
                    GuideItem item = programs[y];
                    if (prevItem != null) {
                        if (item.getStart().before(prevItem.getStop())) {
                            overlapDetected = true;
                        }
                    }
                    prevItem = item;
                    start.add(Calendar.SECOND, 1);
                    startTime.setTime(item.getStart());
                    long pastStart = startTime.getTime().getTime() - start.getTime().getTime();
                    if (y == 0 && pastStart > 0) {
                        Element program_PH = doc.createElement("programme");
                        dateBuff = new StringBuffer();
                        dateFormater.format(start.getTime(), dateBuff, new FieldPosition(0));
                        program_PH.setAttribute("start", dateBuff.toString());
                        dateBuff = new StringBuffer();
                        dateFormater.format(item.getStart(), dateBuff, new FieldPosition(0));
                        program_PH.setAttribute("stop", dateBuff.toString());
                        program_PH.setAttribute("channel", channelName);
                        Element titleElement = doc.createElement("title");
                        Text titleText = doc.createTextNode("EMPTY");
                        titleElement.appendChild(titleText);
                        program_PH.appendChild(titleElement);
                        Element subTitleElement = doc.createElement("sub-title");
                        Text subTitleText = doc.createTextNode("empty");
                        subTitleElement.appendChild(subTitleText);
                        program_PH.appendChild(subTitleElement);
                        Element catElement = doc.createElement("category");
                        Text catText = doc.createTextNode("epgProgramEmpty");
                        catElement.appendChild(catText);
                        program_PH.appendChild(catElement);
                        Element descElement = doc.createElement("desc");
                        Text descText = doc.createTextNode("empty");
                        descElement.appendChild(descText);
                        program_PH.appendChild(descElement);
                        Element lengthElement = doc.createElement("length");
                        lengthElement.setAttribute("units", "minutes");
                        Text lengthText = doc.createTextNode(new Long(pastStart / (1000 * 60) + 1).toString());
                        lengthElement.appendChild(lengthText);
                        program_PH.appendChild(lengthElement);
                        root.appendChild(program_PH);
                    }
                    start.add(Calendar.SECOND, -1);
                    if (y > 0) {
                        long skip = item.getStart().getTime() - (programs[y - 1].getStart().getTime() + (programs[y - 1].getDuration() * 1000 * 60));
                        if (skip > 0) {
                            System.out.println("Skipping : " + skip);
                            Element program_PH = doc.createElement("programme");
                            dateBuff = new StringBuffer();
                            dateFormater.format(programs[y - 1].getStop(), dateBuff, new FieldPosition(0));
                            program_PH.setAttribute("start", dateBuff.toString());
                            dateBuff = new StringBuffer();
                            dateFormater.format(item.getStart(), dateBuff, new FieldPosition(0));
                            program_PH.setAttribute("stop", dateBuff.toString());
                            program_PH.setAttribute("channel", channelName);
                            Element titleElement = doc.createElement("title");
                            Text titleText = doc.createTextNode("EMPTY");
                            titleElement.appendChild(titleText);
                            program_PH.appendChild(titleElement);
                            Element subTitleElement = doc.createElement("sub-title");
                            Text subTitleText = doc.createTextNode("empty");
                            subTitleElement.appendChild(subTitleText);
                            program_PH.appendChild(subTitleElement);
                            Element catElement = doc.createElement("category");
                            Text catText = doc.createTextNode("epgProgramEmpty");
                            catElement.appendChild(catText);
                            program_PH.appendChild(catElement);
                            Element descElement = doc.createElement("desc");
                            Text descText = doc.createTextNode("empty");
                            descElement.appendChild(descText);
                            program_PH.appendChild(descElement);
                            Element lengthElement = doc.createElement("length");
                            lengthElement.setAttribute("units", "minutes");
                            Text lengthText = doc.createTextNode(new Long(skip / (1000 * 60)).toString());
                            lengthElement.appendChild(lengthText);
                            program_PH.appendChild(lengthElement);
                            root.appendChild(program_PH);
                        }
                    }
                    Element program = doc.createElement("programme");
                    dateBuff = new StringBuffer();
                    dateFormater.format(item.getStart(), dateBuff, new FieldPosition(0));
                    program.setAttribute("start", dateBuff.toString());
                    dateBuff = new StringBuffer();
                    dateFormater.format(item.getStop(), dateBuff, new FieldPosition(0));
                    program.setAttribute("stop", dateBuff.toString());
                    program.setAttribute("channel", channelName);
                    Element titleElement = doc.createElement("title");
                    Text titleText = doc.createTextNode(item.getName());
                    titleElement.appendChild(titleText);
                    program.appendChild(titleElement);
                    Element subTitleElement = doc.createElement("sub-title");
                    Text subTitleText = doc.createTextNode(item.getSubName());
                    subTitleElement.appendChild(subTitleText);
                    program.appendChild(subTitleElement);
                    for (int index = 0; index < item.getCategory().size(); index++) {
                        String itemCat = item.getCategory().get(index);
                        Element catElement = doc.createElement("category");
                        Text catText = doc.createTextNode(itemCat);
                        catElement.appendChild(catText);
                        program.appendChild(catElement);
                    }
                    Element descElement = doc.createElement("desc");
                    Text descText = doc.createTextNode(item.getDescription());
                    descElement.appendChild(descText);
                    program.appendChild(descElement);
                    Element lengthElement = doc.createElement("length");
                    lengthElement.setAttribute("units", "minutes");
                    Text lengthText = doc.createTextNode(new Integer(item.getDuration()).toString());
                    lengthElement.appendChild(lengthText);
                    program.appendChild(lengthElement);
                    Element ignoreElement = doc.createElement("ignored");
                    Text ignoreText = null;
                    if (item.getIgnored()) ignoreText = doc.createTextNode("1"); else ignoreText = doc.createTextNode("0");
                    ignoreElement.appendChild(ignoreText);
                    program.appendChild(ignoreElement);
                    String detailsUrl = "/servlet/EpgDataRes?action=06&id=" + item.toString() + "&channel=" + URLEncoder.encode(map[0], "UTF-8");
                    Element infoUrlElement = doc.createElement("detailsUrl");
                    Text infoUrlText = doc.createTextNode(detailsUrl);
                    infoUrlElement.appendChild(infoUrlText);
                    program.appendChild(infoUrlElement);
                    root.appendChild(program);
                }
            }
        }
        boolean overlapWarning = "1".equals(store.getProperty("guide.warn.overlap"));
        if (overlapWarning == true) root.setAttribute("overlapDetected", new Boolean(overlapDetected).toString()); else root.setAttribute("overlapDetected", "false");
        Text textNode = null;
        Element elementNaode = null;
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            String channelName = map[0];
            if (channelName != null) {
                Vector<ScheduleItem> items = new Vector<ScheduleItem>();
                store.getSchedulesWhenInc(start.getTime(), end.getTime(), channelName, items);
                for (int y = 0; y < items.size(); y++) {
                    ScheduleItem item = items.get(y);
                    Element schedule = doc.createElement("schedule");
                    dateBuff = new StringBuffer();
                    dateFormater.format(item.getStart(), dateBuff, new FieldPosition(0));
                    schedule.setAttribute("start", dateBuff.toString());
                    dateBuff = new StringBuffer();
                    dateFormater.format(item.getStop(), dateBuff, new FieldPosition(0));
                    schedule.setAttribute("stop", dateBuff.toString());
                    schedule.setAttribute("duration", new Integer(item.getDuration()).toString());
                    schedule.setAttribute("channel", channelName);
                    startTime.setTime(item.getStart());
                    long pastStart = startTime.getTime().getTime() - start.getTime().getTime();
                    elementNaode = doc.createElement("id");
                    textNode = doc.createTextNode(item.toString());
                    elementNaode.appendChild(textNode);
                    schedule.appendChild(elementNaode);
                    Element topElement = doc.createElement("from_top");
                    Text topText = doc.createTextNode(new Long(pastStart / (1000 * 60)).toString());
                    topElement.appendChild(topText);
                    schedule.appendChild(topElement);
                    Element stateElement = doc.createElement("itemState");
                    Text stateText = doc.createTextNode(new Integer(item.getState()).toString());
                    stateElement.appendChild(stateText);
                    schedule.appendChild(stateElement);
                    if (item.getState() == ScheduleItem.WAITING || item.getState() == ScheduleItem.FINISHED || item.getState() == ScheduleItem.SKIPPED || item.getState() == ScheduleItem.ERROR) {
                        String delUrl = "/servlet/ScheduleDataRes?action=04&id=" + item.toString();
                        addActionUrl(delUrl, item, "D", schedule, doc);
                        if (item.getType() != ScheduleItem.EPG) {
                            String editUrl = "/servlet/ScheduleDataRes?action=01&id=" + item.toString() + "&month=" + startTime.get(Calendar.MONTH) + "&year=" + startTime.get(Calendar.YEAR);
                            addActionUrl(editUrl, item, "E", schedule, doc);
                            String addTimeUrl = "/servlet/ScheduleDataRes?action=05&id=" + item.toString();
                            addActionUrl(addTimeUrl, item, "+", schedule, doc);
                        }
                    } else if (item.getState() == ScheduleItem.RUNNING) {
                        String stopUrl = "/servlet/ScheduleDataRes?action=09&id=" + item.toString();
                        addActionUrl(stopUrl, item, "S", schedule, doc);
                        String addTimeUrl = "/servlet/ScheduleDataRes?action=05&id=" + item.toString();
                        addActionUrl(addTimeUrl, item, "+", schedule, doc);
                    }
                    root.appendChild(schedule);
                }
            }
        }
        String currentEPGTheme = store.getProperty("path.theme.epg");
        XSL transformer = new XSL(doc, currentEPGTheme, urlData, headers);
        String request = urlData.getReqString();
        request = request.replace("&scrollto=-1", "");
        transformer.addCookie("backURL", request);
        return transformer.doTransform(false);
    }
