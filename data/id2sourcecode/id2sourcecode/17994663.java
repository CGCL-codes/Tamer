    private void configure() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    LOGGING.log(Level.INFO, "couldn't get MIDI synthesizer failed");
                    enabled = false;
                    return;
                }
            }
            synthesizer.open();
            receiver = synthesizer.getReceiver();
        } catch (MidiUnavailableException e) {
            LOGGING.log(Level.INFO, "pc speaker disabled", e);
            enabled = false;
            return;
        } catch (Exception e) {
            LOGGING.log(Level.INFO, "pc speaker disabled", e);
            enabled = false;
            return;
        }
        Soundbank sb = synthesizer.getDefaultSoundbank();
        if (sb == null) {
            System.out.println("Warning: loading remote soundbank.");
            try {
                sb = MidiSystem.getSoundbank(new URI("http://www.classicdosgames.com/soundbank.gm").toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sb != null) {
            instruments = sb.getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        MidiChannel[] channels = synthesizer.getChannels();
        cc = channels[0];
        programChange(80);
    }
