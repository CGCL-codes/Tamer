    public static String fetch(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
        String result = fetch(urlConnection);
        urlConnection.disconnect();
        return result;
    }
