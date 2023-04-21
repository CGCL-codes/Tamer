    public void close() {
        try {
            getChannel().close();
        } catch (IOException e) {
        }
        doClose = true;
    }
