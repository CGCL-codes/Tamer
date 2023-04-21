    public static String readWebsite(URL url) {
        HttpURLConnection connection;
        log.warning("Attempting to open URL connection...");
        try {
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not read website for url: " + url.getPath());
        } catch (Exception e) {
            throw new RuntimeException("Could not read website for url: " + url.getPath());
        }
    }
