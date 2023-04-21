    public void parse(URL url) throws Exception {
        BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream()));
        parse(bReader);
    }
