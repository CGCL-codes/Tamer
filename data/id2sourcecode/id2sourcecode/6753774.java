    public static void main(String[] args) throws Exception {
        String prefix = "http://localhost:7000/triplify/near/51.2,13.0/1000";
        int offset = 104936;
        for (int i = 0; i < 50; ++i) {
            Random r = new Random();
            long id = offset + r.nextInt(1000000);
            URL url = new URL(prefix);
            StopWatch sw = new StopWatch();
            sw.start();
            String str = StreamUtil.toString(url.openStream());
            sw.stop();
            System.out.println(str);
            System.out.println("Time taken: " + sw.getTime());
            sw.reset();
        }
    }
