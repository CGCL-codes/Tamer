    private byte[] readHttpConnResponseBody(HttpURLConnection resp) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream respIS = resp.getInputStream();
        int readed;
        while ((readed = respIS.read()) != -1) {
            baos.write(readed);
        }
        baos.close();
        byte[] requestBody = baos.toByteArray();
        return requestBody;
    }
