    public static String getStringFromRest(final String rest) throws IOException {
        final URL url = new URL(rest);
        final InputStream is = url.openStream();
        if (is == null) {
            throw new IOException(String.format("No response from %s", rest));
        }
        final StringBuilder sb = new StringBuilder();
        final Reader r = new InputStreamReader(is, "UTF-8");
        final char[] buf = new char[1028];
        int read;
        do {
            read = r.read(buf, 0, buf.length);
            if (read > 0) {
                sb.append(buf, 0, read);
            }
        } while (read > 0);
        is.close();
        return sb.toString();
    }
