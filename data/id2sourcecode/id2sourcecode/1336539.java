    public void init() throws InvalidContextException {
        if ((rtpReplicator == null) && (rtpTransmitter == null)) throw new InvalidContextException();
        this.speechClient = new SpeechCloudClient(rtpReplicator, rtpTransmitter, url);
        this.telephonyClient = new TelephonyClientImpl(pbxSession.getChannelName());
    }
