    public void init() throws InvalidContextException {
        if (mrcpSession == null) throw new InvalidContextException();
        this.speechClient = new SpeechClientImpl(mrcpSession.getTtsChannel(), mrcpSession.getRecogChannel());
        this.telephonyClient = new TelephonyClientImpl(pbxSession.getChannelName());
    }
