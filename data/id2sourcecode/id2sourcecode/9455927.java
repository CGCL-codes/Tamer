    public byte[] send(byte[] bytes) throws IOException {
        ByteArrayOutputStream responseOut = new ByteArrayOutputStream();
        try {
            OutputStream out;
            InputStream in;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            out = conn.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) throw new java.io.IOException();
            if (conn.getContentLength() > 0) {
                byte[] buffer = new byte[1000];
                in = conn.getInputStream();
                int numRead;
                while ((numRead = in.read(buffer)) != -1) {
                    responseOut.write(buffer, 0, numRead);
                }
                in.close();
            }
        } catch (IOException e) {
            EventLog.exception(e);
            throw (e);
        }
        return responseOut.toByteArray();
    }
