    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage shrtMsg = (ShortMessage) message;
            if (shrtMsg.getCommand() == ShortMessage.NOTE_ON || shrtMsg.getCommand() == ShortMessage.NOTE_OFF) {
                try {
                    if (shrtMsg.getCommand() == ShortMessage.NOTE_ON && shrtMsg.getData2() == 0) shrtMsg.setMessage(ShortMessage.NOTE_OFF, shrtMsg.getChannel(), shrtMsg.getData1(), 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                for (NoteEventListener noteEventListnr : noteEventListeners) noteEventListnr.noteEventOcccurred(shrtMsg);
            } else if (shrtMsg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                for (ControllerEventListener contEventList : controllerEventListeners[((ShortMessage) message).getData1()]) contEventList.controlChange((ShortMessage) message);
            } else if (message.getStatus() == ShortMessage.TIMING_CLOCK && Globals.getMasterSyncMode() == MIDI_SYNC) {
                ++midiClockCount;
                if (midiClockCount == 24) {
                    double newTempo = (double) (60000000000l / (System.nanoTime() - midiClockTime));
                    EventRouter.tempoActionPerformed(new TempoEvent(newTempo));
                    midiClockCount = 0;
                    midiClockTime = System.nanoTime();
                }
            } else if (message.getStatus() == ShortMessage.START && Globals.getMasterSyncMode() == MIDI_SYNC) {
                midiClockCount = 0;
                midiClockTime = System.nanoTime();
                EventRouter.playActionPerformed();
            } else if (message.getStatus() == ShortMessage.STOP && Globals.getMasterSyncMode() == MIDI_SYNC) {
                EventRouter.stopActionPerformed();
            }
        }
    }
