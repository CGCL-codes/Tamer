    public void onFinished() {
        logger.debug("#finished client.id:" + getChannelId());
        Store readPeek = popReadPeekStore();
        readPeek.close();
        Store writePeek = popWritePeekStore();
        writePeek.close();
        super.onFinished();
    }
