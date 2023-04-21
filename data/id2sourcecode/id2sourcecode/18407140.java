    public static final String getFacesConfigVersion(URL url) {
        URLConnection conn = null;
        InputStream input = null;
        String result = "2.0";
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser;
            FacesConfigVersionCheckHandler handler = new FacesConfigVersionCheckHandler();
            factory.setNamespaceAware(false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setValidating(false);
            parser = factory.newSAXParser();
            conn = url.openConnection();
            conn.setUseCaches(false);
            input = conn.getInputStream();
            try {
                parser.parse(input, handler);
            } catch (SAXException e) {
            }
            result = handler.isVersion20OrLater() ? "2.0" : (handler.isVersion12() ? "1.2" : "1.1");
        } catch (Throwable e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Throwable e) {
                }
            }
        }
        return result;
    }
