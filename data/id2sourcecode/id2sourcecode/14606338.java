    public static void main(String[] argv) throws Exception {
        int i;
        URL url = new URL("https://api.sandbox.ebay.com/wsapi");
        URLConnection urlConn = url.openConnection();
        InputStream in = urlConn.getInputStream();
        byte[] buf = new byte[1024];
        while ((i = in.read(buf)) != -1) {
            System.out.print(buf.toString());
        }
    }
