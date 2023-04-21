    public void load(URL url, Component parent) throws Exception {
        if (url != null) {
            URLConnection con = url.openConnection();
            load(con.getInputStream(), url.toString(), parent);
        }
    }
