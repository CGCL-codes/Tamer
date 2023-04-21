    public synchronized void init() throws MidiUnavailableException {
        if (unusedChannels != null) return;
        if (synthesizer == null) synthesizer = MidiSystem.getSynthesizer();
        if (!synthesizer.isOpen()) synthesizer.open();
        MidiChannel[] chn = synthesizer.getChannels();
        playTasks = new ArrayList<PlayTask>();
        if (receiver == null) receiver = synthesizer.getReceiver();
        unusedChannels = new ArrayList<Integer>(chn.length);
        for (int i = 0; i < chn.length; i++) if (i != 9) unusedChannels.add(i);
    }
