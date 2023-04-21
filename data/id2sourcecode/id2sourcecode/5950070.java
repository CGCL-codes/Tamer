    public static String[] getCookieStrings(String urlString) {
        String[] tempCookieStrings = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-TW; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
            tempCookieStrings = tryConnect(connection);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String[] cookieStrings = tempCookieStrings;
        int cookieCount = 0;
        if (tempCookieStrings != null) {
            for (int i = 0; i < tempCookieStrings.length; i++) {
                if (tempCookieStrings[i] != null) {
                    cookieStrings[cookieCount++] = tempCookieStrings[i];
                    System.out.println(cookieCount + ": " + tempCookieStrings[i]);
                }
            }
            Common.debugPrintln("共有" + cookieCount + "串cookie");
        }
        return cookieStrings;
    }
