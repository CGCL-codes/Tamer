    public static void play(URL url) throws IOException {
        try {
            Player player = new Player(url.openStream());
            play(player);
        } catch (JavaLayerException jle) {
            throw new IOException(jle.toString());
        }
    }
