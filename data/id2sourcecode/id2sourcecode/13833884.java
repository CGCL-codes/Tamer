    public void start() throws IllegalStateException {
        try {
            in = url.openStream();
            in.skip(written);
        } catch (IOException e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }
        open = true;
        started = true;
    }
