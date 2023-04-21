    private static final boolean isTaglibDocument20OrLater(URL url) {
        URLConnection conn = null;
        InputStream input = null;
        boolean result = false;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser;
            VersionCheckHandler handler = new VersionCheckHandler();
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
            result = handler.isVersion20OrLater();
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
