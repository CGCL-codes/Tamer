    protected void submitHWaveforms() {
        String h1name = new String(hcurrentfilename + "_H1.w16");
        String h2name = new String(hcurrentfilename + "_H2.w16");
        String h3name = new String(hcurrentfilename + "_H3.w16");
        String h4name = new String(hcurrentfilename + "_H4.w16");
        if (hcurrentfilename.equals("")) {
            System.out.println("There is no waveform file saved.");
        } else {
            Channel h1ch;
            Channel h2ch;
            Channel h3ch;
            Channel h4ch;
            h1ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickH01:7121:FGWAVE");
            h2ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickH02:7121:FGWAVE");
            h3ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickH03:7121:FGWAVE");
            h4ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickH04:7121:FGWAVE");
            h1ch.connectAndWait();
            h2ch.connectAndWait();
            h3ch.connectAndWait();
            h4ch.connectAndWait();
            try {
                h1ch.putVal(h1name);
                h2ch.putVal(h2name);
                h3ch.putVal(h3name);
                h4ch.putVal(h4name);
                Channel.flushIO();
                System.out.println("Loaded filenames: " + h1name + "\t" + h2name + "\t" + h3name + "\t" + h4name);
                fileLabel.setText("Loaded filenames: " + h1name + "\t" + h2name + "\t" + h3name + "\t" + h4name);
            } catch (ConnectionException e) {
                System.err.println("Unable to connect to channel access.");
                fileLabel.setText("Unable to connect to channel access.");
            } catch (PutException e) {
                System.err.println("Unable to set process variables.");
                fileLabel.setText("Unable to set process variables.");
            }
        }
    }
