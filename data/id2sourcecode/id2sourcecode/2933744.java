    public void engineBeatHappened(int engineBeat, double delay, double timeOfBeat) {
        for (int i = 0; i < channels.length; i++) {
            channels[i].getChannelBeat().engineBeatHappened(engineBeat, delay, timeOfBeat);
        }
    }
