    private static Source getSource(Document inputDocument, String inputUrl, String inputString) throws GeneralException, IOException {
        Source source = null;
        if (inputDocument != null) {
            source = new DOMSource(inputDocument);
        } else if (UtilValidate.isNotEmpty(inputString)) {
            source = new StreamSource(new StringReader(inputString));
        } else if (UtilValidate.isNotEmpty(inputUrl)) {
            URL url = FlexibleLocation.resolveLocation(inputUrl);
            URLConnection conn = URLConnector.openConnection(url);
            InputStream in = conn.getInputStream();
            source = new StreamSource(in);
        }
        return source;
    }
