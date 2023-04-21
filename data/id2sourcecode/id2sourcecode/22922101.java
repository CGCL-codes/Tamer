    public static String post(String xml, String urlLocation) throws IOException {
        URL url = getURL(urlLocation);
        if (url == null) return null;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        OutputStream outStream = conn.getOutputStream();
        byte[] bites = (new String(xml)).getBytes();
        outStream.write(bites);
        outStream.flush();
        outStream.close();
        InputStream inStream = conn.getInputStream();
        String responseMsg = __getHttpResponse(inStream);
        inStream.close();
        conn.disconnect();
        return responseMsg;
    }
