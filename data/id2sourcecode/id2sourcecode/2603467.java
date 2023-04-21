    public void onWrittenBody() {
        logger.debug("#writtenBody.cid:" + getChannelId());
        if (asyncFile != null) {
            asyncFile.asyncBuffer(this, asyncFile);
        }
        super.onWrittenBody();
    }
