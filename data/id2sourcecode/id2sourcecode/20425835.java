    public static Map<String, List<String>> getHttpHeaderFields(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        Map<String, List<String>> output = connection.getHeaderFields();
        return output;
    }
