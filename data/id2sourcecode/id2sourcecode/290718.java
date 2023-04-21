    private byte[] remAutoAddItem(HTTPurl urlData, HashMap headers) throws Exception {
        String idString = urlData.getParameter("id");
        int id = -1;
        if (idString != null) {
            try {
                id = Integer.parseInt(idString);
            } catch (Exception e) {
            }
        }
        String all = urlData.getParameter("all");
        if (id > -1) {
            EpgMatch item = (EpgMatch) store.getEpgMatchList().get(id);
            if (item != null) {
                String[] unUsed = noSharedMatchLists(item);
                if (unUsed.length == 0 || (unUsed.length > 0 && "0".equalsIgnoreCase(all))) {
                    store.remEpgMatch(id);
                } else if (unUsed.length > 0 && "1".equalsIgnoreCase(all)) {
                    store.remEpgMatch(id);
                    for (int x = 0; x < unUsed.length; x++) {
                        store.getMatchLists().remove(unUsed[x]);
                    }
                    store.saveMatchList(null);
                } else {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    DOMImplementation di = db.getDOMImplementation();
                    Document doc = di.createDocument("", "buttons", null);
                    Element root = doc.getDocumentElement();
                    root.setAttribute("back", "/servlet/" + urlData.getServletClass());
                    root.setAttribute("title", "Delete any unused Match Lists as Well?");
                    Element button = null;
                    Element elm = null;
                    Text text = null;
                    String actionURL = "";
                    button = doc.createElement("button");
                    button.setAttribute("name", "Yes");
                    elm = doc.createElement("url");
                    actionURL = "/servlet/" + urlData.getServletClass() + "?action=05&id=" + id + "&all=1";
                    text = doc.createTextNode(actionURL);
                    elm.appendChild(text);
                    button.appendChild(elm);
                    root.appendChild(button);
                    button = doc.createElement("button");
                    button.setAttribute("name", "No");
                    elm = doc.createElement("url");
                    actionURL = "/servlet/" + urlData.getServletClass() + "?action=05&id=" + id + "&all=0";
                    text = doc.createTextNode(actionURL);
                    elm.appendChild(text);
                    button.appendChild(elm);
                    root.appendChild(button);
                    XSL transformer = new XSL(doc, "kb-buttons.xsl", urlData, headers);
                    return transformer.doTransform();
                }
            }
        }
        StringBuffer buff = new StringBuffer(256);
        buff.append("HTTP/1.0 302 Moved Temporarily\n");
        buff.append("Location: /servlet/KBAutoAddRes\n\n");
        return buff.toString().getBytes();
    }
