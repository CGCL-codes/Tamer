    public static String get(String u) throws Exception {
        URL url = new URL(u);
        URLConnection connection = url.openConnection();
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
