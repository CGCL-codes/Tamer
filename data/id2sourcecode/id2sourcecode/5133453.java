    public static String doRequest(String strUrl, String request) throws SocketTimeoutException {
        String respons = "";
        if (sslSocketFactory == null) {
            sslSocketFactory = initSSL();
        }
        try {
            URL url = new URL(strUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            respons = sendOutput(conn, request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respons;
    }
