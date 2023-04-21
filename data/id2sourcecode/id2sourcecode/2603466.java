    public void onFinished() {
        logger.debug("#finished.cid:" + getChannelId());
        if (asyncFile != null) {
            asyncFile.close();
            asyncFile = null;
        }
        super.onFinished();
    }
