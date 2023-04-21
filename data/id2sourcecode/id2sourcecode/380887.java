    private byte[] showFiles(HTTPurl urlData, HashMap headers) throws Exception {
        String pathString = urlData.getParameter("path");
        File[] files = null;
        File baseDir = null;
        boolean inBounds = false;
        String[] paths = store.getCapturePaths();
        DllWrapper capEng = new DllWrapper();
        if (pathString != null) {
            File thisPath = new File(pathString);
            String requestedFilePath = thisPath.getCanonicalPath();
            for (int x = 0; x < paths.length; x++) {
                String rootFilePath = new File(paths[x]).getCanonicalPath();
                if (requestedFilePath.indexOf(rootFilePath) == 0) {
                    inBounds = true;
                    break;
                }
            }
        }
        if (inBounds == false) {
            files = new File[paths.length];
            for (int x = 0; x < paths.length; x++) {
                files[x] = new File(paths[x]);
            }
        } else {
            baseDir = new File(pathString);
            String fileMasks = DataStore.getInstance().getProperty("filebrowser.masks");
            files = baseDir.listFiles(new FileTypeFilter(fileMasks));
        }
        NumberFormat nf = NumberFormat.getInstance();
        int count = 0;
        int start = 0;
        int show = 10;
        try {
            show = Integer.parseInt(urlData.getParameter("show"));
        } catch (Exception e) {
        }
        try {
            start = Integer.parseInt(urlData.getParameter("start"));
        } catch (Exception e) {
        }
        if (start < 0) start = 0;
        if (files == null) files = new File[0];
        boolean dirsAtTop = "1".equals(store.getProperty("filebrowser.DirsAtTop"));
        Arrays.sort(files, new CompareFiles(dirsAtTop));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();
        Document doc = di.createDocument("", "files", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("back", "/servlet/ApplyTransformRes?xml=root&xsl=kb-buttons");
        Element fileitem = null;
        Element elm = null;
        Text text = null;
        int fileIndex = start;
        if (inBounds) {
            count++;
            fileitem = doc.createElement("file");
            elm = doc.createElement("name");
            text = doc.createTextNode("<parent>");
            elm.appendChild(text);
            fileitem.appendChild(elm);
            elm = doc.createElement("size");
            elm.setAttribute("units", "");
            text = doc.createTextNode("");
            elm.appendChild(text);
            fileitem.appendChild(elm);
            String action = "/servlet/" + urlData.getServletClass();
            File parent = baseDir.getParentFile();
            if (parent != null) action += "?path=" + URLEncoder.encode(baseDir.getParentFile().getCanonicalPath(), "UTF-8");
            elm = doc.createElement("action");
            text = doc.createTextNode(action);
            elm.appendChild(text);
            fileitem.appendChild(elm);
            root.appendChild(fileitem);
        }
        while (fileIndex < files.length && (count) < show) {
            if (inBounds == false) {
                count++;
                fileitem = doc.createElement("file");
                elm = doc.createElement("name");
                String nameData = files[fileIndex].getCanonicalPath();
                long freeSpace = capEng.getFreeSpace(files[fileIndex].getCanonicalPath());
                nameData += " (" + nf.format((freeSpace / (1024 * 1024))) + " MB Free)";
                text = doc.createTextNode(nameData);
                elm.appendChild(text);
                fileitem.appendChild(elm);
                elm = doc.createElement("size");
                elm.setAttribute("units", "");
                text = doc.createTextNode("");
                elm.appendChild(text);
                fileitem.appendChild(elm);
                String action = "/servlet/" + urlData.getServletClass() + "?path=" + URLEncoder.encode(files[fileIndex].getCanonicalPath(), "UTF-8");
                elm = doc.createElement("action");
                text = doc.createTextNode(action);
                elm.appendChild(text);
                fileitem.appendChild(elm);
                root.appendChild(fileitem);
            } else if (files[fileIndex].isDirectory() && files[fileIndex].isHidden() == false) {
                count++;
                fileitem = doc.createElement("file");
                elm = doc.createElement("name");
                text = doc.createTextNode("<" + files[fileIndex].getName() + ">");
                elm.appendChild(text);
                fileitem.appendChild(elm);
                elm = doc.createElement("size");
                elm.setAttribute("units", "");
                text = doc.createTextNode("");
                elm.appendChild(text);
                fileitem.appendChild(elm);
                String action = "/servlet/" + urlData.getServletClass() + "?path=" + URLEncoder.encode(files[fileIndex].getCanonicalPath(), "UTF-8");
                elm = doc.createElement("action");
                text = doc.createTextNode(action);
                elm.appendChild(text);
                fileitem.appendChild(elm);
                root.appendChild(fileitem);
            } else if (files[fileIndex].isHidden() == false) {
                count++;
                fileitem = doc.createElement("file");
                elm = doc.createElement("name");
                text = doc.createTextNode(files[fileIndex].getName());
                elm.appendChild(text);
                fileitem.appendChild(elm);
                elm = doc.createElement("size");
                elm.setAttribute("units", "KB");
                text = doc.createTextNode(nf.format(files[fileIndex].length() / 1024));
                elm.appendChild(text);
                fileitem.appendChild(elm);
                String action = "/servlet/" + urlData.getServletClass() + "?action=01" + "&file=" + URLEncoder.encode(files[fileIndex].getPath(), "UTF-8") + "&start=" + start;
                elm = doc.createElement("action");
                text = doc.createTextNode(action);
                elm.appendChild(text);
                fileitem.appendChild(elm);
                root.appendChild(fileitem);
            }
            fileIndex++;
        }
        root.setAttribute("start", new Integer(start).toString());
        root.setAttribute("end", new Integer(fileIndex).toString());
        root.setAttribute("show", new Integer(show).toString());
        root.setAttribute("total", new Integer(files.length).toString());
        if (pathString != null) root.setAttribute("path", URLEncoder.encode(new File(pathString).getCanonicalPath(), "UTF-8")); else root.setAttribute("path", "Root");
        XSL transformer = new XSL(doc, "kb-showfiles.xsl", urlData, headers);
        return transformer.doTransform();
    }
