    public static void returnURL(URL url, Writer out) throws IOException {
        URLConnection con = url.openConnection();
        con.connect();
        String encoding = con.getContentEncoding();
        BufferedReader in = null;
        if (encoding == null) {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
        }
        char[] buf = new char[4 * 1024];
        int charsRead;
        while ((charsRead = in.read(buf)) != -1) {
            out.write(buf, 0, charsRead);
        }
    }
