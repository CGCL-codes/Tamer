    public void loadProperties(LibEntryType entryType, URL url, URI uri, String charSet) {
        try {
            Properties properties = new Properties();
            InputStream stream = url.openStream();
            InputStreamReader reader = new InputStreamReader(stream, charSet);
            properties.load(reader);
            load(entryType, properties, url, uri);
            reader.close();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
