    public static String getContent(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        return getContent(urlConnection.getInputStream());
    }
