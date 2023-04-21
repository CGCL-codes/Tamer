    @Override
    public void send(MidiMessage messageIn, long timeStamp) {
        if (messageIn instanceof ShortMessage) {
            ShortMessage sMsg = (ShortMessage) messageIn;
            int command = sMsg.getCommand();
            int channel = sMsg.getChannel();
            int noteValue = sMsg.getData1();
            String key = channel + "_" + noteValue;
            if (command == ShortMessage.NOTE_ON) {
                if (notesMap.get(key) != null) {
                    Thread thread = notesMap.get(key);
                    thread.interrupt();
                }
                Thread thread = new Thread(new NoteGenerator(sMsg, receiverOut, timeStamp));
                notesMap.put(key, thread);
                thread.start();
            } else if (command == ShortMessage.NOTE_OFF) {
                if (notesMap.get(key) != null) {
                    Thread thread = notesMap.get(key);
                    thread.interrupt();
                }
                receiverOut.send(sMsg, timeStamp);
            } else {
                receiverOut.send(sMsg, timeStamp);
            }
        } else {
            receiverOut.send(messageIn, timeStamp);
        }
    }
