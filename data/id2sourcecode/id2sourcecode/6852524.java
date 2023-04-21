    public SidTune loadTune(final URL url) {
        try {
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {

                    public InputStream run() throws IOException {
                        return url.openConnection().getInputStream();
                    }
                });
                tune = SidTune.load(stream);
                tune.getInfo().file = null;
            } catch (PrivilegedActionException e) {
                throw e.getException();
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            tune = null;
            return null;
        }
        return tune;
    }
