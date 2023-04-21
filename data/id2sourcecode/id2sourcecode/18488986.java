    private String submitRequestTo(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        return StreamUtility.readAllFromStream(connection.getInputStream());
    }
