    protected boolean isRemoteAccessibility(String url) {
        try {
            URL targetURL = new URL(url);
            URLConnection urlConnection = targetURL.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            if (urlConnection instanceof HttpURLConnection) {
                int responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
                if (responseCode == 200) return true;
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
