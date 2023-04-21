    public static InputStream openFileOrURL(String fileNameOrUrl) throws IOException {
        try {
            URL url = new URL(fileNameOrUrl);
            return url.openStream();
        } catch (MalformedURLException mfe) {
            InputStream str = new FileInputStream(fileNameOrUrl);
            return str;
        }
    }
