    public static InputStream getStream(final Representation representation) throws IOException {
        if (representation != null) {
            final PipeStream pipe = new PipeStream();
            Thread writer = new Thread() {

                @Override
                public void run() {
                    try {
                        OutputStream os = pipe.getOutputStream();
                        representation.write(os);
                        os.write(-1);
                        os.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            };
            writer.setDaemon(false);
            writer.start();
            return pipe.getInputStream();
        } else {
            return null;
        }
    }
