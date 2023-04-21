    public static boolean checkConnection() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
