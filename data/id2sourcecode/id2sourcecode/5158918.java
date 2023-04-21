    public static String sendRequest(String urlStr, Map<String, Object[]> parameters) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        osw.write(Utils.encodeData(parameters));
        osw.close();
        BufferedInputStream inStream = new BufferedInputStream(connection.getInputStream());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(16384);
        byte[] buffer = new byte[16384];
        int bytesRead;
        while ((bytesRead = inStream.read(buffer, 0, 16384)) > 0) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close();
        inStream.close();
        String ret = outStream.toString("UTF-8");
        connection.disconnect();
        return ret;
    }
