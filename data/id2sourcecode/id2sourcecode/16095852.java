    public void send(MidiMessage message, long timeStamp) {
        ShortMessage sm;
        try {
            sm = (ShortMessage) message;
        } catch (ClassCastException e) {
            return;
        }
        int channelnum = sm.getChannel();
        ChannelInstance dasmc = asmc[channelnum];
        if (sm.getCommand() == ShortMessage.NOTE_ON) {
            dasmc.note = sm.getData1();
            dasmc.velocity = sm.getData2();
            PaintChannel(sm.getChannel(), getGraphics());
        } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
            dasmc.note = 0;
            dasmc.velocity = 0;
            PaintChannel(sm.getChannel(), getGraphics());
        } else if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
            dasmc.Instrument = sm.getData1();
            PaintChannel(sm.getChannel(), getGraphics());
        } else if (sm.getCommand() == ShortMessage.CONTROL_CHANGE) {
            if (sm.getData1() == 7) {
                asmc[sm.getChannel()].channelVelocity = sm.getData2();
                PaintChannel(sm.getChannel(), getGraphics());
            } else if (sm.getData1() == 121) {
                reset(sm.getChannel());
            }
        } else if (sm.getCommand() == ShortMessage.PITCH_BEND) {
            asmc[sm.getChannel()].pitchbend = twobytetoint(sm.getData1(), sm.getData2());
            PaintChannel(sm.getChannel(), getGraphics());
        } else if (sm.getStatus() == sm.SYSTEM_RESET) {
            reset();
        }
    }
