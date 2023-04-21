    private static ObjectInputStream getObjectInputStream(String urlContent) throws Exception {
        URL url = new URL(urlContent);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        return ois;
    }
