    public static CurrentRecord getCurrentRecord(String url) throws MalformedURLException, IOException {
        StringBuffer urlString = new StringBuffer(url);
        if (urlString.lastIndexOf("/") != urlString.length() - 1) {
            urlString.append('/');
        }
        urlString.append("GetCurrentRecord.jsp");
        URLConnection conn = new URL(urlString.toString()).openConnection();
        return CurrentRecord.readObject(conn.getInputStream());
    }
