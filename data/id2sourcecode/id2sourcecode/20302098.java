    private static URLConnection connect(String address) {
        try {
            URL url = new URL(address);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            return urlConnection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
