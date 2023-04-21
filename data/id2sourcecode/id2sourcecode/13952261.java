    public static Vector<String> sendRequest(String request) throws IOException {
        URL url = new URL(props.getProperty("url") + request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "text/xml");
        int code = connection.getResponseCode();
        if (code == 400) {
            Vector<String> responce = readResponce(connection.getErrorStream());
            dumpResponce(responce);
            throw new IOException(responce != null ? responce.get(0) : "");
        }
        return readResponce(connection.getInputStream());
    }
