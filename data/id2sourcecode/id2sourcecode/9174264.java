    @Override
    public void init(Hashtable<String, String> parameters) {
        world = (LM_World) envAgent.getWorld();
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            channel = channels[0];
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
