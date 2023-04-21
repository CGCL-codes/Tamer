    public HttpURLConnection openConnection(URL url, boolean doInput, boolean doOutput, String request, String data, boolean setCookie) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(doInput);
            connection.setDoOutput(doOutput);
            connection.setRequestMethod(request);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.0; de-DE; rv:1.7.5) Gecko/20041122 Firefox/1.0");
            connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            if (data != null) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
            }
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Keep-Alive", "300");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Referer", url.toString());
            if (setCookie) connection.setRequestProperty("Cookie", returnCookies());
            connection.connect();
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
