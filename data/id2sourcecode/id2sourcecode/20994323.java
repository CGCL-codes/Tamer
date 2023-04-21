    public void loadRandomTag() {
        try {
            URL url = new URL("http://000000book.com/data/random.gml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            GMLHandler myExampleHandler = new GMLHandler();
            xr.setContentHandler(myExampleHandler);
            xr.parse(new InputSource(url.openStream()));
            gml = myExampleHandler.getGML();
        } catch (Exception e) {
        }
    }
