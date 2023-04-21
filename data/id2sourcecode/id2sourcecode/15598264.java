    public static InputStream loadFile(URL url) throws IOException {
        InputStream response = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        response = connection.getInputStream();
        return response;
    }
