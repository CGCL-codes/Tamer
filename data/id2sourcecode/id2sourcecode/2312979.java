    public static String getTinyUrl(String fullUrl) {
        String shortURL = fullUrl;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://is.gd/api.php?longurl=" + fullUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setConnectTimeout(7500);
            urlConnection.connect();
            int tucode = urlConnection.getResponseCode();
            if (tucode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    shortURL = reader.readLine();
                } catch (IOException ioe) {
                    Log.error(ioe.getMessage(), ioe);
                } finally {
                    closeQuietly(reader);
                }
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        if (StringUtils.isBlank(shortURL)) {
            return fullUrl;
        }
        return shortURL;
    }
