    private byte[] showAutoEpgAddForm(HTTPurl urlData, HashMap headers) throws Exception {
        Vector list = store.getEpgMatchList();
        EpgMatch epgMatcher = null;
        String index = urlData.getParameter("index");
        if (index == null) index = "";
        int indexOf = -1;
        try {
            indexOf = Integer.parseInt(index);
        } catch (Exception e) {
        }
        if (indexOf > -1 && indexOf < list.size()) {
            epgMatcher = (EpgMatch) list.get(indexOf);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "item_form", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("index", new Integer(indexOf).toString());
        root.setAttribute("delete", "No");
        Element formEl = null;
        formEl = doc.createElement("startBuffer");
        formEl.setAttribute("Name", "Start");
        formEl.setAttribute("max", "59");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "1");
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", new Integer(epgMatcher.getStartBuffer()).toString()); else formEl.setAttribute("value", "5");
        root.appendChild(formEl);
        formEl = doc.createElement("endBuffer");
        formEl.setAttribute("Name", "End");
        formEl.setAttribute("max", "400");
        formEl.setAttribute("min", "0");
        formEl.setAttribute("amount", "5");
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", new Integer(epgMatcher.getEndBuffer()).toString()); else formEl.setAttribute("value", "10");
        root.appendChild(formEl);
        formEl = doc.createElement("referer");
        Text text = doc.createTextNode("/servlet/KBAutoAddRes");
        formEl.appendChild(text);
        root.appendChild(formEl);
        formEl = doc.createElement("captureType");
        getCaptureTypes(doc, formEl);
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", new Integer(epgMatcher.getCaptureType()).toString()); else formEl.setAttribute("value", store.getProperty("Capture.deftype"));
        root.appendChild(formEl);
        formEl = doc.createElement("autoDel");
        formEl.setAttribute("Name", "Auto Delete");
        if (indexOf > -1 && indexOf < list.size()) if (epgMatcher.getAutoDel()) formEl.setAttribute("value", "True"); else formEl.setAttribute("value", "False"); else formEl.setAttribute("value", "False");
        root.appendChild(formEl);
        formEl = doc.createElement("keepfor");
        formEl.setAttribute("Name", "keep For");
        formEl.setAttribute("max", "120");
        formEl.setAttribute("min", "1");
        formEl.setAttribute("amount", "1");
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", new Integer(epgMatcher.getKeepFor()).toString()); else {
            String keep = store.getProperty("AutoDel.KeepFor");
            formEl.setAttribute("value", keep);
        }
        root.appendChild(formEl);
        formEl = doc.createElement("posttask");
        getTaskList(doc, formEl);
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", epgMatcher.getPostTask()); else formEl.setAttribute("value", "");
        root.appendChild(formEl);
        formEl = doc.createElement("filenamePatterns");
        getNamePatterns(doc, formEl);
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", epgMatcher.GetFileNamePattern()); else formEl.setAttribute("value", "");
        root.appendChild(formEl);
        formEl = doc.createElement("capturePaths");
        getCapturePaths(doc, formEl);
        if (indexOf > -1 && indexOf < list.size()) formEl.setAttribute("value", new Integer(epgMatcher.getCapturePathIndex()).toString()); else formEl.setAttribute("value", "-1");
        root.appendChild(formEl);
        XSL transformer = new XSL(doc, "kb-aa-details.xsl", urlData, headers);
        return transformer.doTransform();
    }
