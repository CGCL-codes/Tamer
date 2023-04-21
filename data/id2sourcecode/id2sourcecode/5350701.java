    public static void readUrlTest(String url) throws Exception {
        System.out.println("Initiated reading remote queue URL: " + url);
        InputStream instream = new URL(url).openStream();
        Serializer serializer = new Serializer();
        Queue queue = (Queue) serializer.parse(instream);
        instream.close();
        System.out.println("Completed reading remote queue URL (jobs=" + queue.size() + ")");
    }
