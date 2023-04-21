    public static Document getDocument(URL url) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (db == null) {
            throw new IllegalStateException("Document Builder ERROR.");
        }
        Document dom = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            dom = db.parse(urlConnection.getInputStream());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            urlConnection = null;
        }
        if (dom == null) {
            throw new IllegalStateException("URL " + url.toExternalForm() + ": DOM is not built");
        }
        return dom;
    }
