    protected synchronized void enqueueEvent(MidiMessage msg) throws Exception {
        MidiDevice dev = getMidiOutDevice();
        if (SPOOL_MIDI.getBoolean()) {
            log.finest("sending " + MidiReader.formatMidiMessage(msg));
        }
        if (receiver == null) {
            if (dev == null) {
                if (!synthesizer.isOpen()) {
                    synthesizer.open();
                }
                receiver = synthesizer.getReceiver();
                if (instrument != null) {
                    synthesizer.getChannels()[0].programChange(instrument.getPatch().getBank(), instrument.getPatch().getProgram());
                }
            } else {
                receiver = dev.getReceiver();
            }
        }
        if (msg instanceof MultiMessage) {
            MultiMessage mMsg = (MultiMessage) msg;
            MessageWrapper lastMsg = null;
            for (MessageWrapper mw : mMsg.getMessages()) {
                receiver.send(mw, -1);
                lastMsg = mw;
            }
            if (lastMsg != null && lastMsg.getCommand() == ShortMessage.NOTE_ON) {
                sleep(lastMsg.getDurationMs());
            }
        } else if (msg instanceof MessageWrapper) {
            MessageWrapper mw = (MessageWrapper) msg;
            if (mw.isPause()) {
                sleep(mw.getDurationMs());
            } else {
                receiver.send(mw, -1);
                if (mw.getCommand() == ShortMessage.NOTE_ON) {
                    sleep(mw.getDurationMs());
                }
            }
        } else {
            log.finest("raw midi message");
            receiver.send(msg, -1);
        }
    }
