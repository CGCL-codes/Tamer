    public DataInputStream transmit(String requestFile, String requestParam) throws MalformedURLException, IOException {
        din = null;
        urlString = "http://" + serverHost + ":" + serverPort + "/";
        String connectString = urlString + requestFile;
        if (requestParam == null) connectString = connectString + "?dummy=" + System.currentTimeMillis(); else connectString = connectString + "?" + requestParam + "&dummy=" + System.currentTimeMillis();
        URL url = new URL(connectString);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        din = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        return din;
    }
