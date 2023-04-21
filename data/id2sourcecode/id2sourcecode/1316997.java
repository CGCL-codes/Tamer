    public InputStream call(String address) throws IOException {
        System.out.println(address);
        final URL url = new URL(address);
        final URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }
