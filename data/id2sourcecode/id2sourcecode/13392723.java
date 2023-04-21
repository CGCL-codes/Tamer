    public static void play(File audiofile) {
        try {
            if (audiofile.getName().endsWith(".mid") || audiofile.getName().endsWith(".midi")) {
                Sequence sequence = MidiSystem.getSequence(audiofile);
                if (sequence != null) {
                    try {
                        final Sequencer sequencer = MidiSystem.getSequencer();
                        sequencer.open();
                        sequencer.setSequence(sequence);
                        sequencer.start();
                        new MetaEventListener() {

                            public void meta(MetaMessage ev) {
                                if (ev.getType() == 47) {
                                }
                            }
                        };
                    } catch (MidiUnavailableException ex) {
                        return;
                    }
                }
                return;
            }
            AudioInputStream in = AudioSystem.getAudioInputStream(audiofile);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            rawplay(decodedFormat, din);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
