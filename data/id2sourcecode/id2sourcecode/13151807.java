    private void handleOriginateEvent(OriginateResponseEvent originateEvent) {
        final String traceId;
        final OriginateCallbackData callbackData;
        final OriginateCallback cb;
        final AsteriskChannelImpl channel;
        final AsteriskChannelImpl otherChannel;
        traceId = originateEvent.getActionId();
        if (traceId == null) {
            return;
        }
        synchronized (originateCallbacks) {
            callbackData = originateCallbacks.get(traceId);
            if (callbackData == null) {
                return;
            }
            originateCallbacks.remove(traceId);
        }
        cb = callbackData.getCallback();
        if (!AstUtil.isNull(originateEvent.getUniqueId())) {
            channel = channelManager.getChannelImplById(originateEvent.getUniqueId());
        } else {
            channel = callbackData.getChannel();
        }
        try {
            if (channel == null) {
                final LiveException cause;
                cause = new NoSuchChannelException("Channel '" + callbackData.getOriginateAction().getChannel() + "' is not available");
                cb.onFailure(cause);
                return;
            }
            if (channel.wasInState(ChannelState.UP)) {
                cb.onSuccess(channel);
                return;
            }
            if (channel.wasBusy()) {
                cb.onBusy(channel);
                return;
            }
            otherChannel = channelManager.getOtherSideOfLocalChannel(channel);
            if (otherChannel != null) {
                final AsteriskChannel dialedChannel;
                dialedChannel = otherChannel.getDialedChannel();
                if (otherChannel.wasBusy()) {
                    cb.onBusy(channel);
                    return;
                }
                if (dialedChannel != null && dialedChannel.wasBusy()) {
                    cb.onBusy(channel);
                    return;
                }
            }
            cb.onNoAnswer(channel);
        } catch (Throwable t) {
            logger.warn("Exception dispatching originate progress", t);
        }
    }
