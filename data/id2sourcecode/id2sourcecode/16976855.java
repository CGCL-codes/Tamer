    public static URI unredirect(URI uri) throws IOException {
        if (!REDIRECTOR_DOMAINS.contains(uri.getHost())) {
            return uri;
        }
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setDoInput(false);
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty("User-Agent", "ZXing (Android)");
        try {
            connection.connect();
            switch(connection.getResponseCode()) {
                case HttpURLConnection.HTTP_MULT_CHOICE:
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                case HttpURLConnection.HTTP_SEE_OTHER:
                case 307:
                    String location = connection.getHeaderField("Location");
                    if (location != null) {
                        try {
                            return new URI(location);
                        } catch (URISyntaxException e) {
                        }
                    }
            }
            return uri;
        } finally {
            connection.disconnect();
        }
    }
