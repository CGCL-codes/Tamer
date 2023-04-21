    private byte[] showEPG(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        int year = -1;
        try {
            year = Integer.parseInt(urlData.getParameter("year"));
        } catch (Exception e) {
        }
        if (year == -1) year = now.get(Calendar.YEAR);
        int month = -1;
        try {
            month = Integer.parseInt(urlData.getParameter("month"));
        } catch (Exception e) {
        }
        if (month == -1) month = now.get(Calendar.MONTH) + 1;
        int day = -1;
        try {
            day = Integer.parseInt(urlData.getParameter("day"));
        } catch (Exception e) {
        }
        if (day == -1) day = now.get(Calendar.DATE);
        int startHour = -1;
        try {
            startHour = Integer.parseInt(urlData.getParameter("start"));
        } catch (Exception e) {
        }
        if (startHour == -1) startHour = now.get(Calendar.HOUR_OF_DAY);
        int timeSpan = 3;
        try {
            timeSpan = Integer.parseInt(urlData.getParameter("span"));
        } catch (Exception e) {
        }
        String selected = urlData.getParameter("selected");
        if (selected == null || selected.length() == 0) selected = "";
        SimpleDateFormat df = new SimpleDateFormat("h:mma");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss Z");
        boolean showUnlinked = store.getProperty("epg.showunlinked").equals("1");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "epg", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("year", new Integer(year).toString());
        root.setAttribute("month", new Integer(month).toString());
        root.setAttribute("day", new Integer(day).toString());
        root.setAttribute("start", new Integer(startHour).toString());
        root.setAttribute("show", new Integer(timeSpan).toString());
        Element itemEl = null;
        Element elm = null;
        Text text = null;
        Calendar startPointer = Calendar.getInstance();
        startPointer.set(Calendar.SECOND, 0);
        startPointer.set(Calendar.MINUTE, 0);
        startPointer.set(Calendar.MILLISECOND, 0);
        startPointer.set(Calendar.YEAR, year);
        startPointer.set(Calendar.MONTH, month - 1);
        startPointer.set(Calendar.DATE, day);
        startPointer.set(Calendar.HOUR_OF_DAY, startHour);
        long nowLong = new Date().getTime();
        long startLong = startPointer.getTime().getTime();
        long minPast = (nowLong - startLong) / (1000 * 60);
        if (minPast < (timeSpan * 60) && minPast > -1) {
            itemEl = doc.createElement("now_pointer");
            itemEl.setAttribute("min", new Integer((int) minPast).toString());
            root.appendChild(itemEl);
        }
        int hour = startHour;
        String xm = "am";
        if (hour > 12) hour = startHour - 12;
        if (startHour >= 12) xm = "pm";
        if (hour == 0) hour = 12;
        int min = 0;
        int totalCols = timeSpan * 2;
        for (int x = 0; x < totalCols; x++) {
            itemEl = doc.createElement("time");
            text = doc.createTextNode(hour + ":" + intToStr(min) + xm);
            itemEl.appendChild(text);
            root.appendChild(itemEl);
            min += 30;
            if (min == 60) {
                hour += 1;
                min = 0;
            }
            if (hour == 12 && min == 0) {
                if (xm.equals("am")) xm = "pm"; else xm = "am";
            }
            if (hour == 13) {
                hour = 1;
            }
        }
        GuideStore epgStore = GuideStore.getInstance();
        Vector<String[]> channelMap = epgStore.getChannelMap();
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            Element channel = doc.createElement("channel");
            channel.setAttribute("id", map[0]);
            Element disName = doc.createElement("display-name");
            Text chaName = doc.createTextNode(map[0]);
            disName.appendChild(chaName);
            channel.appendChild(disName);
            root.appendChild(channel);
        }
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DATE, day);
        start.set(Calendar.HOUR_OF_DAY, startHour);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.add(Calendar.SECOND, -1);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(start.getTime());
        end.add(Calendar.HOUR_OF_DAY, timeSpan);
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.MILLISECOND, 0);
        start.add(Calendar.HOUR_OF_DAY, (-1 * (timeSpan - 1)));
        String prevLink = "/servlet/" + urlData.getServletClass() + "?action=01&" + "year=" + start.get(Calendar.YEAR) + "&" + "month=" + (start.get(Calendar.MONTH) + 1) + "&" + "day=" + start.get(Calendar.DATE) + "&" + "start=" + start.get(Calendar.HOUR_OF_DAY) + "&" + "span=" + timeSpan;
        start.add(Calendar.HOUR_OF_DAY, (timeSpan - 1));
        root.setAttribute("title", store.dayName.get(new Integer(start.get(Calendar.DAY_OF_WEEK))) + " (" + start.get(Calendar.DATE) + "/" + (start.get(Calendar.MONTH) + 1) + "/" + start.get(Calendar.YEAR) + ")");
        start.add(Calendar.HOUR_OF_DAY, (timeSpan + 1));
        String nextLink = "/servlet/" + urlData.getServletClass() + "?action=01&" + "year=" + start.get(Calendar.YEAR) + "&" + "month=" + (start.get(Calendar.MONTH) + 1) + "&" + "day=" + start.get(Calendar.DATE) + "&" + "start=" + start.get(Calendar.HOUR_OF_DAY) + "&" + "span=" + timeSpan;
        start.add(Calendar.HOUR_OF_DAY, (-1 * (timeSpan + 1)));
        itemEl = doc.createElement("navigation");
        elm = doc.createElement("next");
        text = doc.createTextNode(nextLink);
        elm.appendChild(text);
        itemEl.appendChild(elm);
        elm = doc.createElement("previous");
        text = doc.createTextNode(prevLink);
        elm.appendChild(text);
        itemEl.appendChild(elm);
        elm = doc.createElement("selected");
        text = doc.createTextNode(selected);
        elm.appendChild(text);
        itemEl.appendChild(elm);
        root.appendChild(itemEl);
        HashMap<String, Vector<ScheduleItem>> schedulesLeftOver = new HashMap<String, Vector<ScheduleItem>>();
        Set<String> wsChannels = store.getChannels().keySet();
        boolean channelMapped = true;
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            String channelName = map[0];
            if (channelName == null || !wsChannels.contains(map[0])) {
                channelName = "Not Mapped";
                channelMapped = false;
            } else {
                channelMapped = true;
                GuideItem[] programs = epgStore.getProgramsInc(start.getTime(), end.getTime(), map[1]);
                Vector<ScheduleItem> schItems = new Vector<ScheduleItem>();
                store.getSchedulesWhenInc(start.getTime(), end.getTime(), channelName, schItems);
                int colCount = 0;
                for (int y = 0; y < programs.length; y++) {
                    GuideItem item = programs[y];
                    start.add(Calendar.SECOND, 1);
                    startTime.setTime(item.getStart());
                    long pastStart = startTime.getTime().getTime() - start.getTime().getTime();
                    if (y == 0 && pastStart > 0) {
                        Element program_PH = doc.createElement("programme");
                        program_PH.setAttribute("start", df.format(start.getTime()));
                        program_PH.setAttribute("stop", df.format(item.getStart()));
                        program_PH.setAttribute("channel", channelName);
                        Element titleElement = doc.createElement("title");
                        Text titleText = doc.createTextNode("EMPTY");
                        titleElement.appendChild(titleText);
                        program_PH.appendChild(titleElement);
                        Element subTitleElement = doc.createElement("sub-title");
                        Text subTitleText = doc.createTextNode("empty");
                        subTitleElement.appendChild(subTitleText);
                        program_PH.appendChild(subTitleElement);
                        Element descElement = doc.createElement("desc");
                        Text descText = doc.createTextNode("empty");
                        descElement.appendChild(descText);
                        program_PH.appendChild(descElement);
                        Element lengthElement = doc.createElement("length");
                        lengthElement.setAttribute("units", "minutes");
                        Text lengthText = doc.createTextNode(new Long(pastStart / (1000 * 60)).toString());
                        lengthElement.appendChild(lengthText);
                        program_PH.appendChild(lengthElement);
                        Element programLengthElement = doc.createElement("programLength");
                        programLengthElement.setAttribute("units", "minutes");
                        Text programLengthText = doc.createTextNode(new Long(pastStart / (1000 * 60)).toString());
                        programLengthElement.appendChild(programLengthText);
                        program_PH.appendChild(programLengthElement);
                        root.appendChild(program_PH);
                        colCount += (int) (pastStart / (1000 * 60));
                    }
                    start.add(Calendar.SECOND, -1);
                    if (y > 0) {
                        long skip = item.getStart().getTime() - (programs[y - 1].getStart().getTime() + (programs[y - 1].getDuration() * 1000 * 60));
                        if (skip > 0) {
                            System.out.println("Skipping : " + skip);
                            Element program_PH = doc.createElement("programme");
                            program_PH.setAttribute("start", df.format(programs[y - 1].getStop()));
                            program_PH.setAttribute("stop", df.format(item.getStart()));
                            program_PH.setAttribute("channel", channelName);
                            Element titleElement = doc.createElement("title");
                            Text titleText = doc.createTextNode("EMPTY");
                            titleElement.appendChild(titleText);
                            program_PH.appendChild(titleElement);
                            Element subTitleElement = doc.createElement("sub-title");
                            Text subTitleText = doc.createTextNode("empty");
                            subTitleElement.appendChild(subTitleText);
                            program_PH.appendChild(subTitleElement);
                            Element descElement = doc.createElement("desc");
                            Text descText = doc.createTextNode("empty");
                            descElement.appendChild(descText);
                            program_PH.appendChild(descElement);
                            Element lengthElement = doc.createElement("length");
                            lengthElement.setAttribute("units", "minutes");
                            Text lengthText = doc.createTextNode(new Long(skip / (1000 * 60)).toString());
                            lengthElement.appendChild(lengthText);
                            program_PH.appendChild(lengthElement);
                            Element programLengthElement = doc.createElement("programLength");
                            programLengthElement.setAttribute("units", "minutes");
                            Text programLengthText = doc.createTextNode(new Long(skip / (1000 * 60)).toString());
                            programLengthElement.appendChild(programLengthText);
                            program_PH.appendChild(programLengthElement);
                            root.appendChild(program_PH);
                            colCount += (int) (skip / (1000 * 60));
                        }
                    }
                    ScheduleItem programSchedule = null;
                    for (int schIndex = 0; schIndex < schItems.size(); schIndex++) {
                        ScheduleItem sch = schItems.get(schIndex);
                        GuideItem createdFrom = sch.getCreatedFrom();
                        if (createdFrom != null) {
                            if (createdFrom.matches(item)) {
                                schItems.remove(schIndex);
                                programSchedule = sch;
                                break;
                            }
                        }
                    }
                    Element program = doc.createElement("programme");
                    program.setAttribute("start", df.format(item.getStart()));
                    program.setAttribute("stop", df.format(item.getStop()));
                    program.setAttribute("channel", channelName);
                    Element titleElement = doc.createElement("title");
                    Text titleText = doc.createTextNode(removeChars(item.getName()));
                    titleElement.appendChild(titleText);
                    program.appendChild(titleElement);
                    Element subTitleElement = doc.createElement("sub-title");
                    Text subTitleText = doc.createTextNode(removeChars(item.getSubName()));
                    subTitleElement.appendChild(subTitleText);
                    program.appendChild(subTitleElement);
                    for (int index = 0; index < item.getCategory().size(); index++) {
                        Element catElement = doc.createElement("category");
                        Text catText = doc.createTextNode(item.getCategory().get(index));
                        catElement.appendChild(catText);
                        program.appendChild(catElement);
                    }
                    Element descElement = doc.createElement("desc");
                    Text descText = doc.createTextNode(removeChars(item.getDescription()));
                    descElement.appendChild(descText);
                    program.appendChild(descElement);
                    int fits = 0;
                    int colSpan = item.getDuration();
                    if (item.getStart().getTime() < start.getTime().getTime() && item.getStop().getTime() > end.getTime().getTime()) {
                        fits = 1;
                        colSpan = (timeSpan * 60);
                    } else if (y == 0 && start.getTime().getTime() > item.getStart().getTime()) {
                        fits = 2;
                        colSpan -= ((start.getTime().getTime() - item.getStart().getTime()) / (1000 * 60)) + 1;
                    } else if (y == programs.length - 1 && (item.getStop().getTime() - 5000) > end.getTime().getTime()) {
                        fits = 3;
                        colSpan = (timeSpan * 60) - colCount;
                    }
                    colCount += colSpan;
                    Element lengthElement = doc.createElement("length");
                    lengthElement.setAttribute("units", "minutes");
                    lengthElement.setAttribute("fits", new Integer(fits).toString());
                    Text lengthText = doc.createTextNode(new Integer(colSpan).toString());
                    lengthElement.appendChild(lengthText);
                    program.appendChild(lengthElement);
                    Element programLengthElement = doc.createElement("programLength");
                    programLengthElement.setAttribute("units", "minutes");
                    Text programLengthText = doc.createTextNode(new Long(item.getDuration()).toString());
                    programLengthElement.appendChild(programLengthText);
                    program.appendChild(programLengthElement);
                    String addLink = "";
                    if (channelMapped) {
                        addLink = "/servlet/KBScheduleDataRes?action=11" + "&channel=" + URLEncoder.encode(map[0], "UTF-8") + "&id=" + item.toString();
                    }
                    Element infoUrlElement = doc.createElement("progAdd");
                    Text infoUrlText = doc.createTextNode(addLink);
                    infoUrlElement.appendChild(infoUrlText);
                    program.appendChild(infoUrlElement);
                    String detailsLink = "";
                    if (channelMapped) {
                        detailsLink = "/servlet/KBEpgDataRes?action=05&" + "channel=" + URLEncoder.encode(map[0], "UTF-8") + "&id=" + URLEncoder.encode(item.toString(), "UTF-8");
                    }
                    Element detailsUrlElement = doc.createElement("showDetails");
                    Text detailsUrlText = doc.createTextNode(detailsLink);
                    detailsUrlElement.appendChild(detailsUrlText);
                    program.appendChild(detailsUrlElement);
                    Element fullTimes = doc.createElement("full_times");
                    fullTimes.setAttribute("start", df2.format(item.getStart()));
                    fullTimes.setAttribute("stop", df2.format(item.getStop()));
                    program.appendChild(fullTimes);
                    Element schElement = doc.createElement("scheduled");
                    if (programSchedule == null) {
                        schElement.setAttribute("state", "-1");
                    } else {
                        schElement.setAttribute("state", new Integer(programSchedule.getState()).toString());
                        Element schElementStart = doc.createElement("start");
                        Text schTextStart = doc.createTextNode(df.format(programSchedule.getStart()));
                        schElementStart.appendChild(schTextStart);
                        schElement.appendChild(schElementStart);
                        Element schElementStop = doc.createElement("stop");
                        Text schTextStop = doc.createTextNode(df.format(programSchedule.getStop()));
                        schElementStop.appendChild(schTextStop);
                        schElement.appendChild(schElementStop);
                    }
                    program.appendChild(schElement);
                    root.appendChild(program);
                }
                Vector<ScheduleItem> remainingItems = schedulesLeftOver.get(channelName);
                if (remainingItems == null) {
                    remainingItems = new Vector<ScheduleItem>();
                    schedulesLeftOver.put(channelName, remainingItems);
                }
                for (int schIndex = 0; schIndex < schItems.size(); schIndex++) {
                    ScheduleItem sch = schItems.get(schIndex);
                    remainingItems.add(sch);
                }
            }
        }
        Text textNode = null;
        Element elementNaode = null;
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            int padding = 0;
            int loops = 0;
            String chanName = map[0];
            if (chanName != null) {
                Vector<ScheduleItem> items = schedulesLeftOver.get(chanName);
                ScheduleItem[] schedules = (ScheduleItem[]) items.toArray(new ScheduleItem[0]);
                while (schedules.length > 0) {
                    Vector<ScheduleItem> overlap = new Vector<ScheduleItem>();
                    int colCount = 0;
                    for (int sch = 0; sch < schedules.length; sch++) {
                        ScheduleItem item = schedules[sch];
                        startTime.setTime(item.getStart());
                        startTime.set(Calendar.MILLISECOND, 0);
                        if (showUnlinked == false && item.getCreatedFrom() != null) {
                        } else if (sch > 0 && ((item.getStart().getTime() < schedules[sch - 1].getStop().getTime() && item.getStart().getTime() > schedules[sch - 1].getStart().getTime()) || (item.getStop().getTime() > schedules[sch - 1].getStart().getTime() && item.getStart().getTime() < schedules[sch - 1].getStop().getTime()))) {
                            overlap.add(item);
                        } else {
                            padding = 0;
                            startTime.setTime(item.getStart());
                            long pastStart = startTime.getTime().getTime() - start.getTime().getTime();
                            if (sch == 0 && pastStart > 0) {
                                padding = (int) (pastStart / (1000 * 60));
                                colCount += (int) (pastStart / (1000 * 60));
                            }
                            start.add(Calendar.SECOND, -1);
                            if (sch > 0) {
                                long skip = item.getStart().getTime() - (schedules[sch - 1].getStart().getTime() + (schedules[sch - 1].getDuration() * 1000 * 60));
                                if (skip > 0) {
                                    System.out.println("Skipping : " + skip);
                                    padding = ((int) (pastStart / (1000 * 60))) - colCount;
                                    colCount += (int) (skip / (1000 * 60));
                                }
                            }
                            int fits = 0;
                            int colSpan = item.getDuration();
                            if (item.getStart().getTime() < start.getTime().getTime() && item.getStop().getTime() > end.getTime().getTime()) {
                                colSpan = (timeSpan * 60);
                                fits = 1;
                            } else if (sch == 0 && start.getTime().getTime() > item.getStart().getTime()) {
                                colSpan -= ((start.getTime().getTime() - item.getStart().getTime()) / (1000 * 60)) + 1;
                                fits = 2;
                            } else if (item.getStop().getTime() > end.getTime().getTime()) {
                                long temp = end.getTime().getTime() - item.getStart().getTime();
                                temp = temp / (1000 * 60);
                                colSpan = (int) temp + 1;
                                fits = 3;
                            }
                            colCount += colSpan;
                            Element schedule = doc.createElement("schedule");
                            schedule.setAttribute("start", df.format(item.getStart()));
                            schedule.setAttribute("stop", df.format(item.getStop()));
                            schedule.setAttribute("duration", new Integer(item.getDuration()).toString());
                            schedule.setAttribute("span", new Integer(colSpan).toString());
                            schedule.setAttribute("prePadding", new Integer(padding).toString());
                            schedule.setAttribute("overlapCount", new Integer(loops).toString());
                            schedule.setAttribute("channel", chanName);
                            schedule.setAttribute("fits", new Integer(fits).toString());
                            elementNaode = doc.createElement("id");
                            textNode = doc.createTextNode(item.toString());
                            elementNaode.appendChild(textNode);
                            schedule.appendChild(elementNaode);
                            elementNaode = doc.createElement("title");
                            textNode = doc.createTextNode(removeChars(item.getName()));
                            elementNaode.appendChild(textNode);
                            schedule.appendChild(elementNaode);
                            elementNaode = doc.createElement("itemState");
                            textNode = doc.createTextNode(new Integer(item.getState()).toString());
                            elementNaode.appendChild(textNode);
                            schedule.appendChild(elementNaode);
                            elementNaode = doc.createElement("itemStatus");
                            textNode = doc.createTextNode(item.getStatus());
                            elementNaode.appendChild(textNode);
                            schedule.appendChild(elementNaode);
                            String action = "/servlet/KBScheduleDataRes?action=04&id=" + item.toString();
                            elementNaode = doc.createElement("progEdit");
                            textNode = doc.createTextNode(action);
                            elementNaode.appendChild(textNode);
                            schedule.appendChild(elementNaode);
                            root.appendChild(schedule);
                        }
                    }
                    schedules = (ScheduleItem[]) overlap.toArray(new ScheduleItem[0]);
                    loops++;
                }
            }
        }
        XSL transformer = new XSL(doc, "kb-epg.xsl", urlData, headers);
        String request = urlData.getReqString();
        int indexOf = request.indexOf("&selected=");
        if (indexOf > -1) {
            request = request.substring(0, indexOf);
        }
        transformer.addCookie("backURL", request);
        return transformer.doTransform();
    }
