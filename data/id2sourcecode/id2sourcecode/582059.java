    public static void main(String[] args) throws Exception {
        Synthesizer synth = MidiSystem.getSynthesizer();
        out("Synthesizer: " + synth);
        synth.open();
        MidiChannel channel = synth.getChannels()[0];
        for (int i = 121; i <= 127; i++) {
            out("controller " + i + ": " + channel.getController(i));
        }
        out("Mono: " + channel.getMono());
        channel.setMono(true);
        for (int i = 121; i <= 127; i++) {
            out("controller " + i + ": " + channel.getController(i));
        }
        out("Mono: " + channel.getMono());
        synth.close();
    }
