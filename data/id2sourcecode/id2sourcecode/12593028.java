    public byte[] getURL(String urlName) {
        URL url;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            m_application.getLogger().logError(8, "getURL: Malformed URL: " + urlName);
            return null;
        }
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setUseCaches(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                m_application.getLogger().logError(8, "getURL: HTTP Connection failed with status: " + statusCode);
                return null;
            }
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                out.write(ch);
            }
            if (out.size() > 0) {
                return out.toByteArray();
            } else {
                return null;
            }
        } catch (Exception e) {
            m_application.getLogger().logError(8, "getURL: HTTP Connection failed with exception: " + e);
            return null;
        }
    }
