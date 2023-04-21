    public static void allNotesOff(Synthesizer synth) {
        try {
            if (!synth.isOpen()) {
                synth.open();
            }
            MidiChannel[] channels = synth.getChannels();
            for (int i = 0; i < channels.length; i++) {
                channels[i].allNotesOff();
            }
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.GENERAL_ERROR);
        }
    }
