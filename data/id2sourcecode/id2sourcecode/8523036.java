    public boolean getRSSEntries(String theUrl, Context contextParam, DBHelper dbParam) {
        context = contextParam;
        db = dbParam;
        sourceUrl = theUrl;
        try {
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xr.setContentHandler(this);
                URL url = new URL(sourceUrl);
                xr.parse(new InputSource(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newData;
    }
