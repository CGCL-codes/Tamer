    URLConnection getURLConnection(String href, String accept, String acceptLanguage) throws IOException {
        URL url = new URL(href);
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) connection;
            http.setInstanceFollowRedirects(true);
            if (accept != null) http.setRequestProperty("Accept", accept);
            if (acceptLanguage != null) http.setRequestProperty("Accept-Language", acceptLanguage);
        }
        return connection;
    }
