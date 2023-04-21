    public static int extractDownloadableVersion(String host, String port) {
        if (host != null && port != null) {
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", port);
        }
        try {
            final URL url = new URL("http://plantuml.sourceforge.net/download.html");
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                final int lastversion = extractVersion(in);
                in.close();
                urlConnection.disconnect();
                return lastversion;
            }
        } catch (IOException e) {
            Log.error(e.toString());
        }
        return -1;
    }
