    public static byte[] get(String site) throws IOException {
        URL url = new URL(site);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return IOUtils.toByteArray(urlConnection.getInputStream());
    }
