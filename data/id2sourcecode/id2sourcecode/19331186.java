    protected Object request(String resource) throws IOException {
        URL url = new URL("https", host, resource);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(createIgnoreCertificateSocketFactory());
        Reader reader = getReader(connection);
        try {
            return JSONValue.parse(reader);
        } finally {
            reader.close();
        }
    }
