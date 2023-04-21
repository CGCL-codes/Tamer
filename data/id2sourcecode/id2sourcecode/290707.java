    private byte[] createAutoAddFromItem(HTTPurl urlData, HashMap headers) throws Exception {
        String backURL = urlData.getCookie("backURL");
        try {
            backURL = URLDecoder.decode(backURL, "UTF-8");
        } catch (Exception e) {
        }
        HashMap<String, EpgMatchList> matchLists = store.getMatchLists();
        String itemID = urlData.getParameter("itemID");
        String wsChan = urlData.getParameter("chan");
        GuideStore guide = GuideStore.getInstance();
        String epgChan = guide.getEpgChannelFromMap(wsChan);
        GuideItem item = guide.getProgram(epgChan, itemID);
        if (item == null) {
            StringBuffer out = new StringBuffer(256);
            out.append("HTTP/1.0 302 Moved Temporarily\n");
            out.append("Location: /servlet/" + urlData.getServletClass() + "?action=01\n\n");
            return out.toString().getBytes();
        }
        String name = item.getName();
        int nextIndex = 0;
        boolean useInt = false;
        String matchListName = name + " (" + wsChan + ")";
        if (matchLists.containsKey(matchListName)) {
            useInt = true;
            while (matchLists.containsKey(matchListName + "_" + nextIndex)) {
                nextIndex++;
            }
        }
        if (useInt) matchListName = matchListName + "_" + nextIndex;
        EpgMatchList newMatchList = new EpgMatchList();
        Vector<EpgMatchListItem> items = newMatchList.getMatchList();
        EpgMatchListItem newItemTitle = new EpgMatchListItem(EpgMatchListItem.TYPE_TEXT);
        newItemTitle.setTextSearchData(name, EpgMatchListItem.FIELD_TITLE, true, EpgMatchListItem.FLAG_CASEINSENSATIVE);
        items.add(newItemTitle);
        EpgMatchListItem newItemChan = new EpgMatchListItem(EpgMatchListItem.TYPE_TEXT);
        newItemChan.setTextSearchData(wsChan, EpgMatchListItem.FIELD_CHANNEL, true, EpgMatchListItem.FLAG_NONE);
        items.add(newItemChan);
        matchLists.put(matchListName, newMatchList);
        store.saveMatchList(null);
        EpgMatch epgMatch = new EpgMatch();
        epgMatch.getMatchListNames().add(matchListName);
        int keepFor = 30;
        try {
            keepFor = Integer.parseInt(store.getProperty("AutoDel.KeepFor"));
        } catch (Exception e) {
        }
        epgMatch.setKeepFor(keepFor);
        epgMatch.setAutoDel(false);
        int startBuff = 0;
        int endBuffer = 0;
        try {
            startBuff = Integer.parseInt(store.getProperty("Schedule.buffer.start"));
            endBuffer = Integer.parseInt(store.getProperty("Schedule.buffer.end"));
        } catch (Exception e) {
        }
        epgMatch.setStartBuffer(startBuff);
        epgMatch.setEndBuffer(endBuffer);
        epgMatch.setPostTask(store.getProperty("Tasks.DefTask"));
        String[] namePatterns = store.getNamePatterns();
        if (namePatterns.length > 0) {
            epgMatch.setFileNamePattern(namePatterns[0]);
        } else {
            epgMatch.setFileNamePattern("(%y-%m-%d %h-%M) %n %c");
        }
        epgMatch.setCaptureType(-1);
        store.addEpgMatch(epgMatch, 0);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "buttons", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("back", "/servlet/KBAutoAddRes");
        root.setAttribute("title", "The Auto-Add item was created and saved. You should probably run the Auto-Add scan now to add any programs that match your new Auto-Add item to the schedule list.");
        Element button = null;
        Element elm = null;
        Text text = null;
        button = doc.createElement("button");
        button.setAttribute("name", "Run Auto-Add Scan Now");
        elm = doc.createElement("url");
        text = doc.createTextNode("/servlet/KBEpgDataRes?action=04");
        elm.appendChild(text);
        button.appendChild(elm);
        root.appendChild(button);
        button = doc.createElement("button");
        button.setAttribute("name", "Return to the EPG");
        elm = doc.createElement("url");
        text = doc.createTextNode(backURL);
        elm.appendChild(text);
        button.appendChild(elm);
        root.appendChild(button);
        XSL transformer = new XSL(doc, "kb-buttons.xsl", urlData, headers);
        return transformer.doTransform();
    }
