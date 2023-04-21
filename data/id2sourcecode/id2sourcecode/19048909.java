    protected void sendContent(final ChannelBuffer buf) {
        if (currentChannelFuture.getChannel().isConnected()) {
            currentChannelFuture = currentChannelFuture.getChannel().write(buf);
        } else {
            if (currentChannelFuture.isSuccess()) {
                logger.error("####Session[" + getID() + "] current remote connection already closed, while chunk size:" + buf.readableBytes());
                closeLocalChannel();
            } else {
                currentChannelFuture.addListener(new ChannelFutureListener() {

                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            future.getChannel().write(buf);
                        } else {
                            logger.error("Remote connection closed.");
                            closeLocalChannel();
                        }
                    }
                });
            }
        }
    }
