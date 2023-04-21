    public static boolean load() {
        try {
            MidiDevice.Info[] devinfo = MidiSystem.getMidiDeviceInfo();
            for (int d = 0; d < devinfo.length; d++) logger.log(Level.INFO, "Device: " + devinfo[d].toString());
            Synth = MidiSystem.getSynthesizer();
            Synth.open();
            channels = Synth.getChannels();
            logger.log(Level.INFO, channels.length + " Channels");
            DefBank = Synth.getDefaultSoundbank();
            if (DefBank != null) {
                logger.log(Level.INFO, "Default Soundbank: " + DefBank.getName());
                Synth.loadAllInstruments(DefBank);
            }
            instruments = Synth.getAvailableInstruments();
            logger.log(Level.INFO, instruments.length + " Instruments");
        } catch (Exception e) {
            logger.log(Level.INFO, "Error loading Synth: " + e.getMessage());
            e.printStackTrace(System.out);
            return false;
        }
        READY = true;
        return true;
    }
