    public SmafEvent[] getSmafEvents(MidiEvent midiEvent, SmafContext context) throws InvalidSmafDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int command = shortMessage.getCommand();
        int data1 = shortMessage.getData1();
        int data2 = shortMessage.getData2();
        if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && data2 == 0)) {
            if (!context.isNoteOffEventUsed()) {
                Debug.println("[" + context.getMidiEventIndex() + "] no pair of ON for: " + channel + "ch, " + data1);
            }
            return null;
        } else {
            MidiEvent noteOffEvent = null;
            try {
                noteOffEvent = context.getNoteOffMidiEvent();
            } catch (NoSuchElementException e) {
                Debug.println(Level.WARNING, "[" + context.getMidiEventIndex() + "] no pair of OFF for: " + channel + "ch, " + data1);
                return null;
            }
            int track = context.retrieveSmafTrack(channel);
            int voice = context.retrieveVoice(channel);
            double scale = context.getScale();
            long currentTick = midiEvent.getTick();
            long noteOffTick = noteOffEvent.getTick();
            int length = (int) Math.round((noteOffTick - currentTick) / scale);
            int delta = context.getDuration();
            int onLength = (length + 254) / 255;
            SmafEvent[] smafEvents = new SmafEvent[1];
            for (int i = 0; i < onLength; i++) {
                NoteMessage smafMessage = new NoteMessage();
                smafMessage.setDuration(i == 0 ? delta : 0);
                smafMessage.setChannel(voice);
                smafMessage.setNote(context.retrievePitch(channel, data1));
                smafMessage.setGateTime(i == onLength - 1 ? length % 255 : 255);
                if (length >= 255) {
                    Debug.println(channel + "ch, " + smafMessage.getNote() + ", " + smafMessage.getDuration() + ":[" + i + "]:" + (i == onLength - 1 ? length % 255 : 255) + "/" + length);
                }
                smafEvents[i] = new SmafEvent(smafMessage, 0l);
                if (smafEvents[i] == null) {
                    Debug.println("[" + i + "]: " + smafEvents[i]);
                }
                if (i == 0) {
                    context.setBeforeTick(track, midiEvent.getTick());
                    break;
                } else {
                }
            }
            ;
            return smafEvents;
        }
    }
