    private void changeMidiFormat() {
        Iterator iterator = midiFragments.iterator();
        while (iterator.hasNext()) {
            MidiEvent[] tempMidiEvents = (MidiEvent[]) iterator.next();
            for (int i = 0; i < tempMidiEvents.length; i++) {
                if (tempMidiEvents[i].getMessage() instanceof ShortMessage) {
                    ShortMessage message = (ShortMessage) tempMidiEvents[i].getMessage();
                    if (message.getCommand() == ShortMessage.NOTE_ON && message.getData2() == 0) {
                        try {
                            message.setMessage(ShortMessage.NOTE_OFF, message.getChannel(), message.getData1(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
