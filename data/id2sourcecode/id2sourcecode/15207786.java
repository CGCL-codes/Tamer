    public boolean isReady() {
        long now = System.currentTimeMillis();
        if (isPrimary) {
            if (now - lastWriteTime >= C4ClientConfiguration.getInstance().getHTTPRequestTimeout()) {
                if (null != clientChannelFuture && clientChannelFuture.getChannel().isConnected()) {
                    clientChannelFuture.getChannel().close();
                }
                return true;
            }
        }
        if (now - lastWriteTime < C4ClientConfiguration.getInstance().getMinWritePeriod()) {
            return false;
        }
        if (null == clientChannelFuture || !clientChannelFuture.getChannel().isConnected()) {
            return true;
        }
        return responseHandler.isTransactionCompeleted();
    }
