    public void parse(URL url) throws IOException {
        URLConnection con = url.openConnection();
        parse(con.getInputStream());
    }
