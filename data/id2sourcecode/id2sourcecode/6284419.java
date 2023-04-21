    public static ArrayList<String> readLines(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        return readLines(new BufferedReader(new InputStreamReader(conn.getInputStream())));
    }
