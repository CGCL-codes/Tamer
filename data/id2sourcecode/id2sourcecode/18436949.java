    public static byte[] readURLByteArray(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            return IOUtils.readByteArray(new BufferedInputStream(connection.getInputStream()));
        } finally {
            connection.disconnect();
        }
    }
