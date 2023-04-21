    private byte[] showItemInstances(HTTPurl urlData, HashMap headers) throws Exception {
        String name = urlData.getParameter("name");
        if (name == null) name = "";
        String start = urlData.getParameter("start");
        if (start == null) start = "0";
        String show = urlData.getParameter("show");
        if (show == null) show = "10";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "buttons", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("title", name);
        root.setAttribute("start", start);
        root.setAttribute("show", show);
        root.setAttribute("back", "/servlet/ApplyTransformRes?xml=epg-index&xsl=kb-buttons");
        Element button = null;
        Element elm = null;
        Text text = null;
        button = doc.createElement("mainurl");
        text = doc.createTextNode("/servlet/KBEpgItemsRes?action=02&name=" + URLEncoder.encode(name, "UTF-8") + "&");
        button.appendChild(text);
        root.appendChild(button);
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d h:mm aa");
        GuideStore guide = GuideStore.getInstance();
        Vector chanMap = guide.getChannelMap();
        for (int q = 0; q < chanMap.size(); q++) {
            String[] map = (String[]) chanMap.get(q);
            GuideItem[] items = guide.getItems(name, map[1]);
            for (int x = 0; x < items.length; x++) {
                String butText = df.format(items[x].getStart());
                if (map[0] != null) butText += " (" + map[0] + ")"; else butText += " (Not Mapped)";
                button = doc.createElement("button");
                button.setAttribute("name", butText);
                elm = doc.createElement("url");
                Calendar cal = Calendar.getInstance();
                cal.setTime(items[x].getStart());
                String buttonUrl = "/servlet/KBEpgDataRes?action=05&channel=" + URLEncoder.encode(map[0], "UTF-8") + "&id=" + items[x].toString();
                text = doc.createTextNode(buttonUrl);
                elm.appendChild(text);
                button.appendChild(elm);
                root.appendChild(button);
            }
        }
        XSL transformer = new XSL(doc, "kb-list.xsl", urlData, headers);
        return transformer.doTransform();
    }
