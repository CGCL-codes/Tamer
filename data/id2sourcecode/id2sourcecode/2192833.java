    private void ReadXML(URL url) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            ItemXMLHandler xmlHandler = new ItemXMLHandler();
            xr.setContentHandler(xmlHandler);
            InputStream xmldata = url.openStream();
            xr.parse(new InputSource(xmldata));
            ItemXMLData parsedXMLDataSet = xmlHandler.getParsedData();
            itemdata = parsedXMLDataSet;
        } catch (Exception e) {
            Log.d("ItemXMLParser::ReadXML", "Error parsing : " + e.getMessage());
            e.printStackTrace();
        }
    }
