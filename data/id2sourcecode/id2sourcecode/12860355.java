    protected void submitVWaveforms() {
        String v1name = new String(vcurrentfilename + "_V1.w16");
        String v2name = new String(vcurrentfilename + "_V2.w16");
        String v3name = new String(vcurrentfilename + "_V3.w16");
        String v4name = new String(vcurrentfilename + "_V4.w16");
        if (vcurrentfilename.equals("")) {
            System.out.println("There is no waveform file saved.");
        } else {
            Channel v1ch;
            Channel v2ch;
            Channel v3ch;
            Channel v4ch;
            v1ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickV01:7121:FGWAVE");
            v2ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickV02:7121:FGWAVE");
            v3ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickV03:7121:FGWAVE");
            v4ch = ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_IKickV04:7121:FGWAVE");
            v1ch.connectAndWait();
            v2ch.connectAndWait();
            v3ch.connectAndWait();
            v4ch.connectAndWait();
            try {
                v1ch.putVal(v1name);
                v2ch.putVal(v2name);
                v3ch.putVal(v3name);
                v4ch.putVal(v4name);
                Channel.flushIO();
                System.out.println("Loaded filenames: " + v1name + "\t" + v2name + "\t" + v3name + "\t" + v4name);
                fileLabel.setText("Loaded filenames: " + v1name + "\t" + v2name + "\t" + v3name + "\t" + v4name);
            } catch (ConnectionException e) {
                System.err.println("Unable to connect to channel access.");
                fileLabel.setText("Unable to connect to channel access.");
            } catch (PutException e) {
                System.err.println("Unable to set process variables.");
                fileLabel.setText("Unable to set process variables.");
            }
        }
    }
