    Reader fileNameToReader(String fileName) throws IOException {
        URL url = null;
        try {
            url = new URL(base, fileName);
        } catch (MalformedURLException ex) {
            throw new IOException("Malformed URL " + ex.getMessage());
        }
        InputStream inStream = url.openStream();
        return new InputStreamReader(inStream);
    }
