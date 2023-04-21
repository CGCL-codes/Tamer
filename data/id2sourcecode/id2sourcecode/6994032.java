    private void setChannelVolume(int channel, int volume) {
        MidiChannel ch = synthesizer.getChannels()[channel];
        ch.controlChange(ControlChangeNumber.CHANNEL_VOLUME_MSB, volume);
    }
