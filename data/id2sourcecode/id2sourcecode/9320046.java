    private void setVolumeSequencer(float volume) {
        int convert = (int) (volume * 127);
        MidiChannel[] channels = synthesizer.getChannels();
        for (MidiChannel channel : channels) {
            channel.controlChange(7, convert);
        }
    }
