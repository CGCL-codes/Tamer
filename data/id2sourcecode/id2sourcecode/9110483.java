    private static String getData(final String address) throws IOException {
        final URL url = new URL(address);
        InputStream html = null;
        try {
            html = url.openStream();
            int c = 0;
            final StringBuilder buffer = new StringBuilder("");
            while (c != -1) {
                c = html.read();
                buffer.append((char) c);
            }
            return buffer.toString();
        } finally {
            if (html != null) {
                html.close();
            }
        }
    }
