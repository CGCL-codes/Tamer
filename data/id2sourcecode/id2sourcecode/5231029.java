    public static URLConnection getConnection(String a_url) throws Exception {
        URL url1 = new URL(a_url);
        URL url = new URL(url1.toExternalForm());
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        return con;
    }
