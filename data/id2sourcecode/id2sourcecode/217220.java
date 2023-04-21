    public static BufferedReader getKongBadgesStream(String category) throws IOException {
        BufferedReader in;
        try {
            URL url = new URL("http://www.kongregate.com/badges" + category);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            in = null;
            throw e;
        }
        return in;
    }
