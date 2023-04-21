    public Integer getRoundTrip(String u) {
        try {
            long tm = System.currentTimeMillis();
            URL url = new URL(u);
            URLConnection connect = url.openConnection();
            connect.connect();
            tm = System.currentTimeMillis() - tm;
            return new Integer(2 * (int) tm);
        } catch (IOException e) {
            return new Integer(-1);
        }
    }
