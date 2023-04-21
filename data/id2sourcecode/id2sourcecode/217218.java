    public static BufferedReader getKongUserInfoStream(String name) throws IOException {
        BufferedReader in;
        try {
            URL url = new URL("http://www.kongregate.com/accounts/" + name.toLowerCase());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            in = null;
            throw e;
        }
        return in;
    }
