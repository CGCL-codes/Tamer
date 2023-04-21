    public static OutputStream openOutputStream(URL url) throws IOException {
        String str = url.toString();
        if (str.startsWith("file:")) {
            try {
                return new FileOutputStream(new File(new URI(str)));
            } catch (Throwable ex) {
            }
        }
        URLConnection conn = url.openConnection();
        conn.setDoInput(false);
        conn.setDoOutput(true);
        return conn.getOutputStream();
    }
