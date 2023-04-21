    public void midiResetGain(double gain) {
        if (gain < 0.0d) gain = 0.0d;
        if (gain > 1.0d) gain = 1.0d;
        int midiVolume = (int) (gain * 127.0d);
        System.err.println("Vol " + midiVolume);
        if (synthesizer != null) {
            javax.sound.midi.MidiChannel[] channels = synthesizer.getChannels();
            System.err.println("Channels: " + channels.length);
            for (int c = 0; channels != null && c < channels.length; c++) {
                System.err.println("cc " + midiVolume);
                channels[c].controlChange(CHANGE_VOLUME, midiVolume);
            }
        } else if (synthDevice != null) {
            try {
                ShortMessage volumeMessage = new ShortMessage();
                for (int i = 0; i < 16; i++) {
                    volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, CHANGE_VOLUME, midiVolume);
                    synthDevice.getReceiver().send(volumeMessage, -1);
                }
            } catch (Exception e) {
                System.err.println("Error resetting gain on MIDI device");
                e.printStackTrace();
            }
        } else if (seqr != null && seqr instanceof Synthesizer) {
            synthesizer = (javax.sound.midi.Synthesizer) seqr;
            javax.sound.midi.MidiChannel[] channels = synthesizer.getChannels();
            for (int c = 0; channels != null && c < channels.length; c++) {
                channels[c].controlChange(CHANGE_VOLUME, midiVolume);
            }
        } else {
            try {
                Receiver receiver = MidiSystem.getReceiver();
                ShortMessage volumeMessage = new ShortMessage();
                for (int c = 0; c < 16; c++) {
                    volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, c, CHANGE_VOLUME, midiVolume);
                    receiver.send(volumeMessage, -1);
                }
            } catch (Exception e) {
                System.err.println("Error resetting gain on MIDI device");
                e.printStackTrace();
            }
        }
    }
