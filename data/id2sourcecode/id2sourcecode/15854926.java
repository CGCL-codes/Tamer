    protected Document getOhlohResponse(String urlString) throws SimalException {
        URLConnection con;
        try {
            URL url = new URL(urlString);
            con = url.openConnection();
            if (!con.getHeaderField("Status").startsWith("200")) {
                throw new SimalException("Unable to open connection to " + url + " status: " + con.getHeaderField("Status"));
            }
        } catch (MalformedURLException e) {
            throw new SimalException("The Ohloh URL is malformed, how can that happen since it is hard coded?", e);
        } catch (IOException e) {
            throw new SimalException("Unable to open connection to Ohloh", e);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = null;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(con.getInputStream());
        } catch (ParserConfigurationException e) {
            throw new SimalException("Unable to configure XML parser", e);
        } catch (SAXException e) {
            throw new SimalException("Unable to parse XML document", e);
        } catch (IOException e) {
            throw new SimalException("Unable to read XML response", e);
        }
        return doc;
    }
