    public static ArrayList<String> loadURLToStrings(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        return loadURLToStrings(connection, -1);
    }
