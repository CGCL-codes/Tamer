    public String getContent(URL url, String User_Agent, String locale, String themes, String domain, String pathinfo) {
        StringBuffer content = new StringBuffer();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("domain", domain);
            connection.setRequestProperty("User-Agent", User_Agent);
            connection.setRequestProperty("pathinfo", pathinfo);
            connection.setRequestProperty("locale", locale);
            connection.setRequestProperty("themes", themes);
            connection.setDoInput(true);
            InputStream is = connection.getInputStream();
            byte[] buffer = new byte[2048];
            int count;
            while (-1 != (count = is.read(buffer))) {
                content.append(new String(buffer, 0, count));
            }
        } catch (IOException e) {
            return null;
        }
        return content.toString();
    }
