    public InputStream get(CGIParameters cgiparams) throws IOException {
        String argString = null;
        if (cgiparams != null && cgiparams.size() > 0) {
            argString = "?" + cgiparams.getEncoded();
        } else argString = "";
        URL url = new URL(cgi.toExternalForm() + argString);
        if (debug) System.out.println("GET:" + cgi.toExternalForm() + argString);
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        return con.getInputStream();
    }
