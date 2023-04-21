    private byte[] showMatchListMenu(HTTPurl urlData, HashMap headers) throws Exception {
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
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "buttons", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("back", "/servlet/" + urlData.getServletClass() + "?action=02&index=" + index);
        root.setAttribute("title", "Auto-Add Match List Menu");
        Element button = null;
        Element elm = null;
        Text text = null;
        String actionURL = "";
        button = doc.createElement("button");
        button.setAttribute("name", "Back");
        elm = doc.createElement("url");
        actionURL = "/servlet/" + urlData.getServletClass() + "?action=02&index=" + index;
        text = doc.createTextNode(actionURL);
        elm.appendChild(text);
        button.appendChild(elm);
        root.appendChild(button);
        if (item.getMatchListNames().size() > 0) {
            button = doc.createElement("button");
            button.setAttribute("name", "Show Current");
            elm = doc.createElement("url");
            actionURL = "/servlet/" + urlData.getServletClass() + "?action=10&index=" + index;
            text = doc.createTextNode(actionURL);
            elm.appendChild(text);
            button.appendChild(elm);
            root.appendChild(button);
        }
        if (store.getMatchLists().size() > 0) {
            button = doc.createElement("button");
            button.setAttribute("name", "Add");
            elm = doc.createElement("url");
            actionURL = "/servlet/" + urlData.getServletClass() + "?action=09&index=" + index;
            text = doc.createTextNode(actionURL);
            elm.appendChild(text);
            button.appendChild(elm);
            root.appendChild(button);
        }
        XSL transformer = new XSL(doc, "kb-buttons.xsl", urlData, headers);
        return transformer.doTransform();
    }
