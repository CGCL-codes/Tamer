    public static String getContentPost(String url, String param, String code) {
        HttpURLConnection connect = null;
        try {
            URL myurl = new URL(url);
            connect = (HttpURLConnection) myurl.openConnection();
            connect.setConnectTimeout(30000);
            connect.setReadTimeout(30000);
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("POST");
            connect.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 1.1.4322; .NET CLR 2.0.50727; Alexa Toolbar; MAXTHON 2.0)");
            OutputStreamWriter out = new OutputStreamWriter(connect.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            return StringUtil.convertStreamToString(connect.getInputStream(), code);
        } catch (Exception e) {
            slogger.warn(e.getMessage());
        } finally {
            if (connect != null) {
                connect.disconnect();
            }
        }
        return null;
    }
