    public static boolean urlIsOK(String urlString) {
        boolean isOK = false;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-TW; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isOK = true;
                Common.debugPrintln(urlString + " 測試連線結果: OK");
            } else {
                isOK = false;
                Common.debugPrintln(urlString + " 測試連線結果: 不OK ( " + connection.getResponseCode() + " )");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOK;
    }
