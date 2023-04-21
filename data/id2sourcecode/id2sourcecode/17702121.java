    @Override
    public final void push(AKSpeak speak) throws InvalidMessageException {
        int channel = speak.getChannel();
        if (channel != channelID) {
            throw new InvalidMessageException("Tried to push '" + speak + "' to channel " + channelID);
        }
        Logger.debug("Pushing " + speak + " through channel " + channelID);
        speak = applyInputNoise(speak);
        Logger.debug("Input noise result: " + speak);
        if (speak != null) {
            pushImpl(speak);
        }
    }
