    private byte[] showAddMatchList(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        int index = -1;
        try {
            index = Integer.parseInt(urlData.getParameter("index"));
        } catch (Exception e) {
        }
        EpgMatch item = (EpgMatch) store.getEpgMatchList().get(index);
        if (item == null) {
            String out = "HTTP/1.0 302 Moved Temporarily\nLocation: " + "/servlet/" + urlData.getServletClass() + "\n\n";
            return out.getBytes();
        }
        String start = urlData.getParameter("start");
        if (start == null || start.length() == 0) start = "0";
        String show = urlData.getParameter("show");
        if (show == null || show.length() == 0) show = "10";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "buttons", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("start", start);
        root.setAttribute("show", show);
        root.setAttribute("back", "/servlet/" + urlData.getServletClass() + "?action=08&index=" + index);
        root.setAttribute("title", "Select a Match List to Add it");
        Element button = null;
        Element elm = null;
        Text text = null;
        button = doc.createElement("mainurl");
        text = doc.createTextNode("/servlet/" + urlData.getServletClass() + "?action=09&index=" + index + "&");
        button.appendChild(text);
        root.appendChild(button);
        HashMap<String, EpgMatchList> matches = store.getMatchLists();
        String[] keys = (String[]) matches.keySet().toArray(new String[0]);
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        int total = 0;
        for (int x = 0; x < keys.length; x++) {
            String action = "/servlet/KBAutoAddRes?action=12&index=" + index + "&name=" + URLEncoder.encode(keys[x], "UTF-8");
            button = doc.createElement("button");
            button.setAttribute("name", keys[x]);
            elm = doc.createElement("url");
            text = doc.createTextNode(action);
            elm.appendChild(text);
            button.appendChild(elm);
            elm = doc.createElement("confirm");
            text = doc.createTextNode("true");
            elm.appendChild(text);
            button.appendChild(elm);
            root.appendChild(button);
            total++;
        }
        root.setAttribute("total", new Integer(total).toString());
        XSL transformer = new XSL(doc, "kb-list.xsl", urlData, headers);
        return transformer.doTransform();
    }
