    @Override
    public void onWaveletUpdate(ProtocolWaveletUpdate update) {
        if (shouldAccept(update)) {
            if (update.hasResultingVersion()) {
                versions.updateHistory(getTarget(update), update.getResultingVersion());
            }
            if (update.hasChannelId() && (update.hasCommitNotice() || update.hasMarker() || update.hasSnapshot() || update.getAppliedDeltaSize() > 0)) {
                ProtocolWaveletUpdate fake = ProtocolWaveletUpdateJsoImpl.create();
                fake.setChannelId(update.getChannelId());
                update.clearChannelId();
                callback.onUpdate(deserialize(fake));
                callback.onUpdate(deserialize(update));
            } else {
                callback.onUpdate(deserialize(update));
            }
        }
    }
