    public void placePlaybackCall(Message message, final ProgressListener callListener) {
        LOG.debug("place playback call " + message);
        IPbxProvider provider = createPbxProvider(message.getPbxProfile());
        CallParameters callParameters = new CallParameters(provider.getAdministratorAddress(), new PlayMessageChannelApplet(message), "", "");
        IPbxCall call = provider.createCall(callParameters);
        IPbxCallObserver observer = new MyTestCallProgressObserver(callParameters.getChannelAddress(), callListener);
        call.addObserver(observer);
        call.connect();
    }
