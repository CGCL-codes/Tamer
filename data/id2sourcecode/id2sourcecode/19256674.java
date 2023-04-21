    @Override
    public void onUpdate(WaveViewServiceUpdate update) {
        try {
            checkUpdateProtocolRestrictions(update);
        } catch (ChannelException e) {
            triggerOnException(e, update.hasWaveletId() ? update.getWaveletId() : null);
            terminate("View update raised exception: " + e.toString());
        }
        switch(state) {
            case INITIAL:
                Preconditions.illegalState("Unexpected update before view channel opened: %s, update: %s", this, update);
                break;
            case CONNECTING:
                if (!update.hasChannelId()) {
                    onException(new ChannelException("First update did not contain channel id. Wave id: " + waveId + ", update: " + update, Recoverable.NOT_RECOVERABLE));
                    return;
                }
                channelId = update.getChannelId();
                state = State.CONNECTED;
                if (openListener != null) {
                    openListener.onConnected();
                }
                break;
            case CONNECTED:
                if (update.hasChannelId()) {
                    logger.trace().log("A non-first update contained a channel id: " + update);
                }
                if (openListener != null) {
                    WaveletId waveletId = update.hasWaveletId() ? update.getWaveletId() : null;
                    HashedVersion lastCommittedVersion = update.hasLastCommittedVersion() ? update.getLastCommittedVersion() : null;
                    HashedVersion currentVersion = update.hasCurrentVersion() ? update.getCurrentVersion() : null;
                    try {
                        if (update.hasWaveletSnapshot()) {
                            openListener.onSnapshot(waveletId, update.getWaveletSnapshot(), lastCommittedVersion, currentVersion);
                        } else if (update.hasDeltas() || update.hasLastCommittedVersion() || update.hasCurrentVersion()) {
                            openListener.onUpdate(waveletId, update.getDeltaList(), lastCommittedVersion, currentVersion);
                        }
                        if (update.hasMarker()) {
                            openListener.onOpenFinished();
                        }
                    } catch (ChannelException e) {
                        triggerOnException(e, waveletId);
                        terminate("View update raised exception: " + e.toString());
                    }
                }
                break;
            case CLOSING:
                if (!hasChannelId()) {
                    if (!update.hasChannelId()) {
                        onException(new ChannelException("First update did not contain channel id. Wave id: " + waveId + ", update: " + update, Recoverable.NOT_RECOVERABLE));
                    }
                    channelId = update.getChannelId();
                    requestViewClose();
                }
                state = State.CLOSED;
                break;
            case CLOSED:
                break;
            default:
                Preconditions.illegalState("update in unknown state" + state);
        }
    }
