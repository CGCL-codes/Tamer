    private String getContent(String httpUrl) {
        String htmlCode = "";
        try {
            InputStream in;
            URL url = new java.net.URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.connect();
            in = connection.getInputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = in.read(buffer, 0, 512)) != -1) {
                htmlCode += new String(buffer, 0, length);
            }
        } catch (Exception e) {
        }
        if (htmlCode == null) {
            return null;
        }
        return htmlCode;
    }
