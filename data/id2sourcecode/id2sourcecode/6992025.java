    private Reader openURL(String fn) throws IOException {
        URL url = new URL(fn);
        URLConnection con = url.openConnection();
        con.setDoInput(true);
        return new InputStreamReader(con.getInputStream());
    }
