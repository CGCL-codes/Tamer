    public boolean satisfy() {
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(milli);
            urlConnection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
