    protected InputStream sendBuffer(URL url, ByteArrayOutputStream outBuffer) throws IOException, OFXConnectionException {
        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-ofx");
        connection.setRequestProperty("Content-Length", String.valueOf(outBuffer.size()));
        connection.setRequestProperty("Accept", "*/*, application/x-ofx");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = connection.getOutputStream();
        out.write(outBuffer.toByteArray());
        InputStream in;
        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            in = connection.getInputStream();
        } else if (responseCode >= 400 && responseCode < 500) {
            throw new OFXServerException("Error with client request: " + connection.getResponseMessage(), responseCode);
        } else {
            throw new OFXServerException("Invalid response code from OFX server: " + connection.getResponseMessage(), responseCode);
        }
        return in;
    }
