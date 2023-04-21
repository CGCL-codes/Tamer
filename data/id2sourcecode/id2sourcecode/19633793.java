    public Document getDocument(URL url) throws IOException, ParserConfigurationException, SAXException {
        Object document = this.documentMap.get(url);
        if (document == null) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            try {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                document = docBuilder.parse(inputStream);
                if (this.documentMap.size() == this.maxDocuments) {
                    this.documentMap.clear();
                }
                this.documentMap.put(url, document);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("Document cache warning: Could not " + "close XML input stream");
                }
            }
        }
        return (Document) document;
    }
