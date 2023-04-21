    public static ReadableByteChannel getChannel(final Representation representation) throws IOException {
        final Pipe pipe = Pipe.open();
        Thread writer = new Thread() {

            @Override
            public void run() {
                try {
                    WritableByteChannel wbc = pipe.sink();
                    representation.write(wbc);
                    wbc.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        writer.setDaemon(false);
        writer.start();
        return pipe.source();
    }
