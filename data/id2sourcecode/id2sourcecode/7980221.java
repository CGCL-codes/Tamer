    public static List<GenericPOI> getPOIs(double lat, double lon, int maxPOIs) {
        List<GenericPOI> list = null;
        try {
            URL url = new URL(buildUrl(lat, lon, maxPOIs));
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            TagParser parser = new TagParser();
            xr.setContentHandler(parser);
            xr.parse(new InputSource(url.openStream()));
            list = parser.getGenericPOIs();
        } catch (Exception e) {
            Log.e("ARTags", "POIService", e);
        }
        return list;
    }
