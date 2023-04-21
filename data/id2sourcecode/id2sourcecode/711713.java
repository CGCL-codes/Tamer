    private byte[] showSearchForm(HTTPurl urlData, HashMap headers) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "search", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("back", "/servlet/ApplyTransformRes?xml=epg-index&xsl=kb-buttons");
        Element formEl = doc.createElement("channel");
        getChannelList(doc, formEl);
        formEl.setAttribute("value", "Any");
        root.appendChild(formEl);
        formEl = doc.createElement("category");
        getCatList(doc, formEl);
        formEl.setAttribute("value", "Any");
        root.appendChild(formEl);
        XSL transformer = new XSL(doc, "kb-SearchEpg.xsl", urlData, headers);
        return transformer.doTransform();
    }
