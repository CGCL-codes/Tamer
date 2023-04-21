    public boolean exists(URI uri, Map<?, ?> options) {
        try {
            URL url = new URL(uri.toString());
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("HEAD");
                int responseCode = httpURLConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } else {
                InputStream inputStream = urlConnection.getInputStream();
                inputStream.close();
                return true;
            }
        } catch (Throwable exception) {
            return false;
        }
    }
