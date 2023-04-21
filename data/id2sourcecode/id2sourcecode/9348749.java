    public static int getCode(String url_address) {
        int code = -1;
        try {
            URL url = new URL(url_address);
            HttpsURLConnection cn = (HttpsURLConnection) url.openConnection();
            cn.connect();
            code = cn.getResponseCode();
            cn.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("httpsConnetionCode:Error[" + e + "]");
            return code;
        } catch (Exception e) {
            System.out.println("httpsConnetionCode:Error[" + e + "]");
            return code;
        }
        return code;
    }
