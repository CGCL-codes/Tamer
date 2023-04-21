    public final HttpURLConnection getConnection() throws Exception {
        if (con == null) {
            con = (HttpURLConnection) url.openConnection();
            setConnectionProperties(con);
        }
        return con;
    }
