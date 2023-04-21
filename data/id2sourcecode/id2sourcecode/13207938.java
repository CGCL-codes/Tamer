    public void searchResults(HTTPurl urlData, HashMap<String, String> headers, OutputStream out) throws Exception {
        String name = urlData.getParameter("q");
        if (name == null || name.length() == 0) name = "";
        String type = urlData.getParameter("type");
        if (type == null || type.length() == 0) type = "Title";
        int searchType = 0;
        if ("title".equalsIgnoreCase(type)) searchType = 1; else if ("description".equalsIgnoreCase(type)) searchType = 2; else searchType = 0;
        String cat = urlData.getParameter("cat");
        if (cat == null || cat.length() == 0) cat = "any";
        String chan = urlData.getParameter("chan");
        if (chan == null || chan.length() == 0) chan = "any";
        GuideStore guide = GuideStore.getInstance();
        HashMap<String, Vector<GuideItem>> results = new HashMap<String, Vector<GuideItem>>();
        guide.simpleEpgSearch(name, searchType, cat, chan, 0, null, results);
        String[] keys = (String[]) results.keySet().toArray(new String[0]);
        Vector<String[]> channelMap = guide.getChannelMap();
        XmlDoc xmlDoc = new XmlDoc("epg_search");
        for (int y = 0; y < keys.length; y++) {
            Vector<GuideItem> result = results.get(keys[y]);
            if (result != null && result.size() > 0) {
                for (int x = 0; x < result.size(); x++) {
                    GuideItem item = (GuideItem) result.get(x);
                    String epgChannel = "not_mapped";
                    for (int q = 0; q < channelMap.size(); q++) {
                        if (channelMap.get(q)[0].equals(keys[y])) {
                            epgChannel = channelMap.get(q)[1];
                            break;
                        }
                    }
                    Element program = xmlDoc.createElement("epg_item");
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
                    Element programLengthElement = xmlDoc.createTextElement("duration-length", new Long(item.getDuration()).toString());
                    program.appendChild(programLengthElement);
                    Element fullTimes = xmlDoc.createElement("times");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(item.getStart());
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
                    channels.setAttribute("chan-display-name", keys[y]);
                    channels.setAttribute("chan-epgdata-name", epgChannel);
                    program.appendChild(channels);
                    xmlDoc.getRoot().appendChild(program);
                }
            }
        }
        out.write(xmlDoc.getDocBytes());
    }
