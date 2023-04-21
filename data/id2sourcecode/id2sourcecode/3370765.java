    public void updateDisplay() {
        if (!synth_loaded) {
            displayLab.setText("<html><body>Initializing . . .");
        } else {
            MidiDevice.Info info = softsynth.getDeviceInfo();
            String fmts = (int) format.getSampleRate() + "Hz " + format.getSampleSizeInBits() + "bit " + format.getChannels() + "ch";
            String line1 = "<b>" + info.getName() + " " + info.getVersion() + "</b> &nbsp;" + fmts;
            String line2 = "";
            if (sbk == null) {
                line2 = "No SoundBank Loaded!";
            } else {
                if (sbk_errmsg != null) {
                    line2 = sbk_errmsg;
                } else if (sbkfile == null) {
                    line2 = "Default SoundBank";
                } else {
                    line2 = sbkfile.getName();
                }
                if (line2.length() > 31) {
                    line2 = line2.substring(0, 31);
                }
            }
            String line3 = "";
            if (seq == null) {
                line3 = "No Sequence";
            } else {
                if (seqr.isRunning() || seqr.getTickPosition() != 0) {
                    long a = seqr.getTickPosition() / seq.getResolution();
                    long b = seqr.getTickLength() / seq.getResolution();
                    if (seqr.isRunning()) {
                        line3 = "PLAY " + a + " of " + b;
                    } else {
                        line3 = "STOP " + a + " of " + b;
                    }
                } else {
                    if (seq_errmsg != null) {
                        line3 = seq_errmsg;
                    } else {
                        line3 = seqfile.getName();
                    }
                    if (line3.length() > 31) {
                        line3 = line3.substring(0, 31);
                    }
                }
            }
            displayLab.setText("<html><body>" + line1 + "<br>" + line2 + "<br>" + line3);
        }
    }
