    public static String getActualIp() throws IOException {
        URL url = new URL(GET_IP);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        byte[] b = new byte[512];
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = in.read(b)) >= 0) {
            sb.append(new String(b, 0, ch));
        }
        return sb.toString();
    }
