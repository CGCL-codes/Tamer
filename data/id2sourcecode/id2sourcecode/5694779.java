    private String request(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }
