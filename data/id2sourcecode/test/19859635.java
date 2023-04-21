    private static void stream(String u) {
        URL url = null;
        try {
            url = new URL(u);
        } catch (MalformedURLException mue) {
            System.out.println("The URL is invalid.");
            System.exit(1);
        }
        InputStream gsmStream = null;
        InputStream auStream = null;
        try {
            gsmStream = url.openStream();
        } catch (IOException ioe) {
            System.err.println("IO exception occured.");
            System.exit(1);
        }
        auStream = new GSMDecoderStream(gsmStream);
        int i;
        int x;
        byte[] b = new byte[1];
        AudioPlayer.player.start(auStream);
    }
