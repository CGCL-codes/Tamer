    public static void main(String[] args) {
        BlueSentry bs;
        if (args.length > 0) bs = new BlueSentry(args[0]); else bs = new BlueSentry();
        bs.DEBUG = true;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                bs.getOutputStream().write(in.readLine().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
