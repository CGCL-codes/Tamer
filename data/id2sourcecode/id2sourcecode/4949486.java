    private void open() throws MidiUnavailableException {
        synth = MidiSystem.getSynthesizer();
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        MidiDevice.Info msInfo = null;
        StringBuilder sb = new StringBuilder();
        sb.append("Available MidiDevice are\n");
        for (MidiDevice.Info i : infos) {
            if (i.toString().contains("Microsoft GS Wavetable Synth")) {
                msInfo = i;
                sb.append(" *****");
            }
            sb.append("\t" + i.toString() + ": " + i.getDescription() + '\n');
        }
        synth.open();
        sb.append("synth=" + synth.getDeviceInfo().toString() + " with default soundbank " + synth.getDefaultSoundbank().getDescription() + '\n');
        sb.append("max synthesizer latency =" + synth.getLatency() + " us\n");
        log.info(sb.toString());
        channels = synth.getChannels();
        channel = channels[PERCUSSION_CHANNEL];
    }
