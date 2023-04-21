    private long getCurrentHttpTokenTime() {
        setCurrentToken(null);
        try {
            final URL url = new URL("http://unixtime.forsthaus.de/time.php");
            final URLConnection conn = url.openConnection();
            final InputStream istream = conn.getInputStream();
            try {
                final StringBuilder sb = new StringBuilder();
                int ch = -1;
                while ((ch = istream.read()) != -1) {
                    sb.append((char) ch);
                }
                long l1 = Long.parseLong(sb.toString());
                long ctt = l1 / 60;
                setCurrentTokenTime(ctt);
                setNextNewToken(System.currentTimeMillis() + ((ctt + 1) * 60 - l1) * 1000);
                return getCurrentTokenTime();
            } catch (NumberFormatException e) {
            } finally {
                istream.close();
            }
        } catch (IOException e) {
        }
        return System.currentTimeMillis() / 60000;
    }
