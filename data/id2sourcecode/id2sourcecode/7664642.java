    public static void main(String argv[]) throws Exception {
        int b;
        ContentLengthUpdater os = new ContentLengthUpdater(System.out, Long.parseLong(argv[0]));
        while ((b = System.in.read()) >= 0) os.write(b);
        os.flush();
    }
