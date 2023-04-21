    public static void main(String[] args) {
        if (args.length != 1) usage();
        RSSHandler handler = new RSSHandler(args[0]);
        System.out.println(handler.getChannel());
    }
