    private void connect(byte[] bData) throws U2000ProcessException {
        System.out.println("Connecting to: " + url.toString());
        String SOAPAction = "";
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;
            httpConn.setRequestProperty("Content-Length", String.valueOf(bData.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
        } catch (IOException ioExp) {
            CommonLogger.error(this, ioExp.getMessage());
            throw new U2000ProcessException(ioExp.getMessage());
        }
    }
