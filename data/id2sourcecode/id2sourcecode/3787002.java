    protected TreeModel createIndex() {
        final TocHandler handler = new TocHandler(base_);
        URL url = null;
        try {
            url = getToc();
        } catch (final MalformedURLException _e1) {
            FuLog.warning(_e1);
            return null;
        }
        if (url == null) {
            return null;
        }
        lastToc_ = url.toExternalForm();
        InputStream io = null;
        try {
            io = url.openStream();
            final SAXParserFactory parser = SAXParserFactory.newInstance();
            parser.setNamespaceAware(false);
            parser.setValidating(false);
            final SAXParser saxparser = parser.newSAXParser();
            final XMLReader reader = saxparser.getXMLReader();
            reader.setContentHandler(handler);
            final InputSource s = new InputSource(io);
            s.setEncoding("UTF-8");
            saxparser.getXMLReader().parse(s);
            home_ = handler.getHome();
        } catch (final Exception _e) {
            FuLog.warning(_e);
        } finally {
            try {
                if (io != null) {
                    io.close();
                }
            } catch (final IOException _e) {
                FuLog.warning(_e);
            }
        }
        return handler.getTreeModel();
    }
