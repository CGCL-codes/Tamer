    public static void parse(URL url, DefaultHandler handler) throws ParserConfigurationException, SAXException, IOException {
        TimeMeasure tm = UtilTime.createTimeMeasure("xml factory");
        InputStream is = url.openStream();
        try {
            SAXParserFactory factory = UtilSAX.getInstance();
            tm.finish();
            tm = UtilTime.createTimeMeasure("parser");
            SAXParser saxParser = factory.newSAXParser();
            tm.finish();
            tm = UtilTime.createTimeMeasure("parse");
            saxParser.parse(is, handler);
            tm.finish();
        } finally {
            is.close();
        }
    }
