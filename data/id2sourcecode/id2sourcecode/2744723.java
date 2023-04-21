    public static Double getWebserverResponseTime(String u) {
        Double t;
        long time = System.currentTimeMillis();
        try {
            u = u.replaceAll("\"", "");
            URL url = new URL(u);
            URLConnection connect = url.openConnection();
            connect.connect();
            t = new Double((double) (System.currentTimeMillis() - time));
            System.out.println(new Long(System.currentTimeMillis() - time));
            return t;
        } catch (IOException e) {
            t = Double.POSITIVE_INFINITY;
            return t;
        }
    }
