    public static Boolean testWebserver(String u) {
        try {
            u = u.replaceAll("\"", "");
            URL url = new URL(u);
            URLConnection connect = url.openConnection();
            connect.connect();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }
