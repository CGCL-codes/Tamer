    public static String readUrl(URL url) throws IOException {
        java.io.BufferedReader is = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
        StringBuffer sb = new StringBuffer();
        int c = is.read();
        while (c != -1) {
            sb.append((char) c);
            c = is.read();
        }
        return sb.toString();
    }
