    public MfiEvent[] getMfiEvents(MidiEvent midiEvent, MfiContext context) throws InvalidMfiDataException {
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
            int track = context.retrieveMfiTrack(channel);
            int voice = context.retrieveVoice(channel);
            double scale = context.getScale();
            long currentTick = midiEvent.getTick();
            long noteOffTick = noteOffEvent.getTick();
            int length = (int) Math.round((noteOffTick - currentTick) / scale);
            if (length == 0) {
                Debug.println(Level.WARNING, "length is 0 ~ 1, " + MidiUtil.paramString(shortMessage) + ", " + ((noteOffTick - currentTick) / scale));
            } else if (length < 0) {
                Debug.println(Level.WARNING, "length < 0, " + MidiUtil.paramString(shortMessage) + ", " + ((noteOffTick - currentTick) / scale));
            }
            int delta = context.getDelta(context.retrieveMfiTrack(channel));
            int onLength = (length + 254) / 255;
            MfiEvent[] mfiEvents = new MfiEvent[1];
            for (int i = 0; i < Math.max(onLength, 1); i++) {
                NoteMessage mfiMessage = new VaviNoteMessage();
                mfiMessage.setDelta(i == 0 ? delta : 0);
                mfiMessage.setVoice(voice);
                mfiMessage.setNote(context.retrievePitch(channel, data1));
                mfiMessage.setGateTime(i == onLength - 1 ? length % 255 : 255);
                mfiMessage.setVelocity(data2 / 2);
                if (length >= 255) {
                    Debug.println(channel + "ch, " + mfiMessage.getNote() + ", " + mfiMessage.getDelta() + ":[" + i + "]:" + (i == onLength - 1 ? length % 255 : 255) + "/" + length);
                }
                mfiEvents[i] = new MfiEvent(mfiMessage, 0l);
                if (i == 0) {
                    context.setPreviousTick(track, midiEvent.getTick());
                    break;
                } else {
                }
            }
            return mfiEvents;
        }
    }
