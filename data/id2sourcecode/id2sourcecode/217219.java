    public static BufferedReader getKongUserBadgeStream(String name, String sort) throws IOException {
        BufferedReader in;
        try {
            URL url = new URL("http://www.kongregate.com/accounts/" + name.toLowerCase() + "/badges?sort=" + sort);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            in = null;
            throw e;
        }
        return in;
    }
