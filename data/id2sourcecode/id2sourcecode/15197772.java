    @Override
    public void channelDisconnected(ChannelHandlerContext aCtx, ChannelStateEvent aEvent) throws Exception {
        if (mLog.isDebugEnabled()) {
            mLog.debug("Channel is disconnected");
        }
        mChannels.remove(aEvent.getChannel());
        this.mContext = aCtx;
        super.channelDisconnected(aCtx, aEvent);
        mEngine.connectorStopped(mConnector, CloseReason.CLIENT);
    }
