    protected Document parseDocument(URL url) throws IOException, SAXException {
        InputStream inputStream = url.openStream();
        try {
            return documentBuilder.parse(inputStream);
        } finally {
            inputStream.close();
        }
    }
