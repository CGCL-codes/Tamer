    private ByteArrayOutputStream executePost(byte[] bytes) throws IOException {
        URL url = new URL(_storeURI);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data");
        connection.setRequestProperty("Content-length", "" + bytes.length);
        connection.setRequestProperty("Connection", "close");
        connection.getOutputStream().write(bytes);
        connection.getOutputStream().close();
        ByteArrayOutputStream outStream = getReply(connection.getInputStream());
        connection.disconnect();
        return outStream;
    }
