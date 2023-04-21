    public static Document readWebsite(URL url) {
        HttpURLConnection connection;
        try {
            log.warning("Attempting to open URL connection...");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            return docBuilder.parse(inputStream);
        } catch (IOException e) {
            log.severe(e.getMessage());
        } catch (SAXException e) {
            log.severe(e.getMessage());
        } catch (ParserConfigurationException e) {
            log.severe(e.getMessage());
        }
        throw new RuntimeException("Could not read website for url: " + url.getPath());
    }
