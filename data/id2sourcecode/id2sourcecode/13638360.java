    private Map getZipI18nStrings() {
        Map map = null;
        boolean isError = false;
        try {
            URL url = getClass().getResource("/rj/tools/jcsc/ui/JcscI18n.xml");
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(url.openStream()));
            Document document = parser.getDocument();
            map = new XMLI18nMap(document, "english");
        } catch (SAXException e) {
            isError = true;
        } catch (IOException e) {
            isError = true;
        }
        if (isError) {
            throw new RuntimeException("'JcscI18n.xml' could not be found in the JCSC.jar file");
        }
        return map;
    }
