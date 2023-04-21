    public String getHtmlCode(String httpUrl) {
        String htmlCode = "";
        HttpURLConnection connection = null;
        try {
            InputStream in;
            URL url = new java.net.URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Accept-Encoding", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                java.io.BufferedReader breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String currentLine;
                Pattern p = Pattern.compile("charset=utf-8\"", Pattern.CASE_INSENSITIVE);
                while ((currentLine = breader.readLine()) != null) {
                    Matcher m = p.matcher(currentLine);
                    currentLine = m.replaceAll("charset=gb2312\"");
                    htmlCode += "\n" + currentLine;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return htmlCode;
    }
