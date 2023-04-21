    private long getCurrentHttpTokenTime() {
        final String urlString = "http://unixtime.forsthaus.de/time.php";
        try {
            final URL url = new URL(urlString);
            final URLConnection conn = url.openConnection();
            final InputStream istream = conn.getInputStream();
            try {
                final StringBuilder sb = new StringBuilder();
                int ch = -1;
                while ((ch = istream.read()) != -1) {
                    sb.append((char) ch);
                }
                final long l1 = Long.parseLong(sb.toString());
                return l1 * 1000;
            } catch (final NumberFormatException e) {
                throw new RuntimeException(e);
            } finally {
                istream.close();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
