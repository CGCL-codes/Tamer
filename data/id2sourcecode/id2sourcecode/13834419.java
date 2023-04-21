    public AudioStreamVoice(final AudioServer audioServer, final FrinikaSequencer sequencer, final AudioReader ais, final long clipStartTimePosition1) throws Exception {
        super(audioServer, 0);
        this.sampleRate = audioServer.getSampleRate();
        this.ais = ais;
        setRealStartTime(clipStartTimePosition1);
        nChannel = ais.getFormat().getChannels();
        sequencer.addSongPositionListener(new SongPositionListener() {

            public void notifyTickPosition(long tick) {
                setRunning(sequencer.isRunning());
                setFramePos(getFramePos(sequencer, audioServer, clipStartPositionInMillis));
            }

            public boolean requiresNotificationOnEachTick() {
                return false;
            }
        });
    }
