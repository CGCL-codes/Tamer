    public InputStream sendGetMessage(Properties args, String encode) throws IOException {
        if (encode == null || "null".equalsIgnoreCase(encode)) encode = Environment.DefaultEncode;
        String argString = "";
        if (args != null) {
            argString = "?" + toEncodedString(args, encode);
        }
        URL url = new URL(servlet.toExternalForm() + argString);
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        sendHeaders(con);
        return con.getInputStream();
    }
