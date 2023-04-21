    public Vector<SoundLibraryEntry> executeQuery() throws Exception {
        String url_string = page_url + query;
        url_string = url_string.replace(" ", "%20");
        url_connection = new URL(url_string);
        url_connection.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            document = builder.parse(url_connection.openStream());
        } catch (Exception e) {
            throw e;
        }
        results = new Vector<SoundLibraryEntry>();
        NodeList entries = document.getElementsByTagName("Entry");
        for (int i = 0; i < entries.getLength(); i++) {
            Node entry = entries.item(i);
            Node data = entry.getFirstChild();
            URL url = new URL((String) data.getTextContent());
            data = data.getNextSibling();
            String title = (String) data.getTextContent();
            data = data.getNextSibling();
            String author = (String) data.getTextContent();
            data = data.getNextSibling();
            String genre = (String) data.getTextContent();
            data = data.getNextSibling();
            String tags = (String) data.getTextContent();
            data = data.getNextSibling();
            String date = (String) data.getTextContent();
            data = data.getNextSibling();
            String sequence = (String) data.getTextContent();
            results.add(new SoundLibraryEntry(url, title, author, genre, tags, date, sequence));
        }
        return (results);
    }
