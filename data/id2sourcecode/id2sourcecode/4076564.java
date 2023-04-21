    public static Stats make(URL url) throws IOException {
        System.out.println(new Date() + " Opening connection to URL");
        URLConnection con = url.openConnection();
        System.out.println(new Date() + " Getting content length");
        int size = con.getContentLength();
        return size == -1 ? new BasicStats() : new ProgressStats(size);
    }
