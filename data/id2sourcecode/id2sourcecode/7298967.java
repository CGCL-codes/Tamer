    public String createPropertiesForURI(String uri) {
        try {
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException exception) {
                url = new URL("file:" + uri);
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            byte[] input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
            return new String(input, "ISO-8859-1");
        } catch (IOException exception) {
        }
        return null;
    }
