    public static OutputStream createOutputStream(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }
