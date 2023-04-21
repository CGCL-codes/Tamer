    public static void FindXML() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(new URL(url).openStream());
        } catch (ParserConfigurationException pce) {
        } catch (SAXException se) {
        } catch (IOException ioe) {
        }
        parseCurrencies();
    }
