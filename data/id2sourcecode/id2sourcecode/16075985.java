        @Override
        protected void filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {
            if (message instanceof ShortMessage) {
                final ShortMessage sMsg = (ShortMessage) message;
                for (final NoteViewerBox box : listeners) {
                    if (sMsg.getCommand() == ShortMessage.NOTE_OFF || (sMsg.getCommand() == ShortMessage.NOTE_ON && sMsg.getData2() == 0x0)) {
                        box.noteOff(sMsg.getChannel(), sMsg.getData1());
                    } else if (sMsg.getCommand() == ShortMessage.NOTE_ON) {
                        box.noteOn(sMsg.getChannel(), sMsg.getData1());
                    }
                }
            }
            sendNow(message);
        }
