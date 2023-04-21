    public static InputStream getInputStream(URL codeBase, String filePath) throws IOException, MalformedURLException {
        URL url = new URL(codeBase + filePath);
        InputStream inputStream = url.openStream();
        return inputStream;
    }
