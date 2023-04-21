    public String getShortUrl(String urlName) {
        try {
            URL url = new URL("http://is.gd/api.php?longurl=" + URLEncoder.encode(urlName, "UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return reader.readLine();
            } else {
                return "";
            }
        } catch (Exception e) {
            log.severe("Exception encountered: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
