    private boolean isValidUrl() {
        URL url;
        try {
            url = new URL(cruiseUrl);
            if (url != null) {
                URLConnection connection = url.openConnection();
                connection.connect();
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
