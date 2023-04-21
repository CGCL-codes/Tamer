    public void getEpgData(HTTPurl urlData, HashMap<String, String> headers, OutputStream out) throws Exception {
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
        XmlDoc xmlDoc = new XmlDoc("epg");
        xmlDoc.getRoot().setAttribute("year", new Integer(year).toString());
        xmlDoc.getRoot().setAttribute("month", new Integer(month).toString());
        xmlDoc.getRoot().setAttribute("day", new Integer(day).toString());
        xmlDoc.getRoot().setAttribute("hour", new Integer(startHour).toString());
        xmlDoc.getRoot().setAttribute("span", new Integer(timeSpan).toString());
        Calendar startPointer = Calendar.getInstance();
        startPointer.set(Calendar.SECOND, 0);
        startPointer.set(Calendar.MINUTE, 0);
        startPointer.set(Calendar.MILLISECOND, 0);
        startPointer.set(Calendar.YEAR, year);
        startPointer.set(Calendar.MONTH, month - 1);
        startPointer.set(Calendar.DATE, day);
        startPointer.set(Calendar.HOUR_OF_DAY, startHour);
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
        GuideStore epgStore = GuideStore.getInstance();
        Vector<String[]> channelMap = epgStore.getChannelMap();
        Set<String> wsChannels = store.getChannels().keySet();
        for (int x = 0; x < channelMap.size(); x++) {
            Element channel = xmlDoc.createElement("channel");
            Element programsElem = xmlDoc.createElement("programs");
            String[] map = (String[]) channelMap.get(x);
            String channelName = map[0];
            if (channelName != null && wsChannels.contains(map[0])) {
                channel.setAttribute("display-name", map[0]);
                channel.setAttribute("epgdata-name", map[1]);
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
                        Element program_PH = xmlDoc.createElement("program");
                        Element idElement = xmlDoc.createTextElement("id", "-1");
                        program_PH.appendChild(idElement);
                        Element titleElement = xmlDoc.createTextElement("title", "EMPTY");
                        program_PH.appendChild(titleElement);
                        Element subTitleElement = xmlDoc.createTextElement("sub-title", "EMPTY");
                        program_PH.appendChild(subTitleElement);
                        Element descElement = xmlDoc.createTextElement("desc", "EMPTY");
                        program_PH.appendChild(descElement);
                        String lengthText = new Long(pastStart / (1000 * 60)).toString();
                        Element lengthElement = xmlDoc.createTextElement("length", lengthText);
                        lengthElement.setAttribute("units", "minutes");
                        program_PH.appendChild(lengthElement);
                        String plString = new Long(pastStart / (1000 * 60)).toString();
                        Element programLengthElement = xmlDoc.createTextElement("programLength", plString);
                        programLengthElement.setAttribute("units", "minutes");
                        program_PH.appendChild(programLengthElement);
                        programsElem.appendChild(program_PH);
                        colCount += (int) (pastStart / (1000 * 60));
                    }
                    start.add(Calendar.SECOND, -1);
                    if (y > 0) {
                        long skip = item.getStart().getTime() - (programs[y - 1].getStart().getTime() + (programs[y - 1].getDuration() * 1000 * 60));
                        if (skip > 0) {
                            System.out.println("Skipping : " + skip);
                            Element program_PH = xmlDoc.createElement("program");
                            Element idElement = xmlDoc.createTextElement("id", "-1");
                            program_PH.appendChild(idElement);
                            Element titleElement = xmlDoc.createTextElement("title", "EMPTY");
                            program_PH.appendChild(titleElement);
                            Element subTitleElement = xmlDoc.createTextElement("sub-title", "EMPTY");
                            program_PH.appendChild(subTitleElement);
                            Element descElement = xmlDoc.createTextElement("desc", "EMPTY");
                            program_PH.appendChild(descElement);
                            String lengthText = new Long(skip / (1000 * 60)).toString();
                            Element lengthElement = xmlDoc.createTextElement("length", lengthText);
                            lengthElement.setAttribute("units", "minutes");
                            program_PH.appendChild(lengthElement);
                            String programLengthText = new Long(skip / (1000 * 60)).toString();
                            Element programLengthElement = xmlDoc.createTextElement("programLength", programLengthText);
                            programLengthElement.setAttribute("units", "minutes");
                            program_PH.appendChild(programLengthElement);
                            programsElem.appendChild(program_PH);
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
                    Element program = xmlDoc.createElement("program");
                    Element idElement = xmlDoc.createTextElement("id", item.toString());
                    program.appendChild(idElement);
                    Element titleElement = xmlDoc.createTextElement("title", removeChars(item.getName()));
                    program.appendChild(titleElement);
                    Element subTitleElement = xmlDoc.createTextElement("sub-title", item.getSubName());
                    program.appendChild(subTitleElement);
                    for (int index = 0; index < item.getCategory().size(); index++) {
                        Element catElement = xmlDoc.createTextElement("category", item.getCategory().get(index));
                        program.appendChild(catElement);
                    }
                    Element descElement = xmlDoc.createTextElement("desc", removeChars(item.getDescription()));
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
                    Element lengthElement = xmlDoc.createTextElement("display-length", new Integer(colSpan).toString());
                    lengthElement.setAttribute("fits", new Integer(fits).toString());
                    program.appendChild(lengthElement);
                    Element programLengthElement = xmlDoc.createTextElement("duration-length", new Long(item.getDuration()).toString());
                    program.appendChild(programLengthElement);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(item.getStart());
                    Element fullTimes = xmlDoc.createElement("times");
                    Element time = xmlDoc.createElement("start");
                    time.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
                    time.setAttribute("month", new Integer(cal.get(Calendar.MONTH) + 1).toString());
                    time.setAttribute("day", new Integer(cal.get(Calendar.DATE)).toString());
                    time.setAttribute("hour", new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString());
                    time.setAttribute("minute", new Integer(cal.get(Calendar.MINUTE)).toString());
                    fullTimes.appendChild(time);
                    cal.setTime(item.getStop());
                    time = xmlDoc.createElement("stop");
                    time.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
                    time.setAttribute("month", new Integer(cal.get(Calendar.MONTH) + 1).toString());
                    time.setAttribute("day", new Integer(cal.get(Calendar.DATE)).toString());
                    time.setAttribute("hour", new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString());
                    time.setAttribute("minute", new Integer(cal.get(Calendar.MINUTE)).toString());
                    fullTimes.appendChild(time);
                    program.appendChild(fullTimes);
                    Element channels = xmlDoc.createElement("program_channel");
                    channels.setAttribute("chan-display-name", map[0]);
                    channels.setAttribute("chan-epgdata-name", map[1]);
                    program.appendChild(channels);
                    Element schElement = xmlDoc.createElement("scheduled");
                    if (programSchedule == null) {
                        schElement.setAttribute("state", "-1");
                        schElement.setAttribute("id", "-1");
                    } else {
                        schElement.setAttribute("state", new Integer(programSchedule.getState()).toString());
                        schElement.setAttribute("id", programSchedule.toString());
                    }
                    program.appendChild(schElement);
                    programsElem.appendChild(program);
                }
                channel.appendChild(programsElem);
            }
            xmlDoc.getRoot().appendChild(channel);
        }
        out.write(xmlDoc.getDocBytes());
    }
