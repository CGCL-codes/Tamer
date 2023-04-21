    private byte[] runAutoAddTest(HTTPurl urlData, HashMap headers) throws Exception {
        int index = Integer.parseInt(urlData.getParameter("index"));
        EpgMatch epgMatcher = (EpgMatch) store.getEpgMatchList().get(index);
        HashMap<String, Vector<GuideItem>> results = new HashMap<String, Vector<GuideItem>>();
        Vector matchNames = epgMatcher.getMatchListNames();
        HashMap matchLists = store.getMatchLists();
        GuideStore guide = GuideStore.getInstance();
        EpgMatchList matcher = null;
        for (int nameIndex = 0; nameIndex < matchNames.size(); nameIndex++) {
            String matchListName = (String) matchNames.get(nameIndex);
            matcher = (EpgMatchList) matchLists.get(matchListName);
            if (matcher != null) {
                guide.searchEPG(matcher, results);
            }
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "buttons", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("id", "");
        root.setAttribute("url", "/servlet/KBAutoAddRes?action=02&index=" + index);
        root.setAttribute("filter", "");
        Element logitem = null;
        Element elm = null;
        Text text = null;
        Vector channelMap = guide.getChannelMap();
        Set wsChannels = store.getChannels().keySet();
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d h:mm aa");
        int count = 0;
        for (int y = 0; y < channelMap.size(); y++) {
            String[] map = (String[]) channelMap.get(y);
            Vector result = (Vector) results.get(map[0]);
            if (result.size() > 0 && wsChannels.contains(map[0])) {
                logitem = doc.createElement("logitem");
                logitem.setAttribute("type", "1");
                elm = doc.createElement("line");
                text = doc.createTextNode(map[0]);
                elm.appendChild(text);
                logitem.appendChild(elm);
                root.appendChild(logitem);
                for (int x = 0; x < result.size(); x++) {
                    GuideItem item = (GuideItem) result.get(x);
                    logitem = doc.createElement("logitem");
                    logitem.setAttribute("type", "0");
                    elm = doc.createElement("line");
                    String matchText = item.getName();
                    matchText += " (" + df.format(item.getStart(), new StringBuffer(), new FieldPosition(0)).toString() + ")";
                    text = doc.createTextNode(matchText);
                    elm.appendChild(text);
                    logitem.appendChild(elm);
                    root.appendChild(logitem);
                    count++;
                }
            }
        }
        if (count == 0) {
            logitem = doc.createElement("logitem");
            logitem.setAttribute("type", "0");
            elm = doc.createElement("line");
            text = doc.createTextNode("No Hits");
            elm.appendChild(text);
            logitem.appendChild(elm);
            root.appendChild(logitem);
        }
        XSL transformer = new XSL(doc, "kb-searchtest.xsl", urlData, headers);
        return transformer.doTransform();
    }
