    public static InputStream getInputStream(URL url) throws Exception {
        URLConnection con = url.openConnection();
        con.setAllowUserInteraction(false);
        con.connect();
        InputStream is = con.getInputStream();
        return is;
    }
