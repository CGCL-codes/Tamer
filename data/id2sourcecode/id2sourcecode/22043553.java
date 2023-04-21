    public static OrbisStreamer play(URL url) throws IOException {
        InputStream in = url.openStream();
        OrbisStreamer streamer = new OrbisStreamer(in);
        try {
            synchronized (streamer) {
                streamer.start();
                streamer.wait(OPEN_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new IOException("interrupted: " + e);
        }
        if (streamer.isRunning()) {
            return streamer;
        } else {
            if (streamer.failure instanceof IOException) {
                throw (IOException) streamer.failure;
            } else if (streamer.failure instanceof RuntimeException) {
                throw (RuntimeException) streamer.failure;
            } else if (streamer.failure instanceof Error) {
                throw (Error) streamer.failure;
            } else if (streamer.failure != null) {
                throw new IOException("unknown failure: " + streamer.failure);
            }
            return null;
        }
    }
