    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage shortMsg = (ShortMessage) message;
            int channel = shortMsg.getChannel();
            int noteNum = shortMsg.getData1();
            int velocity = shortMsg.getData2();
            if (processor == null || arrangement == null || channel >= arrangement.size()) {
                return;
            }
            String id = arrangement.get(channel).arrangementId;
            String score = "i";
            switch(shortMsg.getCommand()) {
                case ShortMessage.NOTE_ON:
                    if (velocity > 0) {
                        score = processor.getNoteOn(id, noteNum, noteNum, velocity);
                    } else {
                        score = processor.getNoteOff(id, noteNum);
                    }
                    break;
                case ShortMessage.NOTE_OFF:
                    score = processor.getNoteOff(id, noteNum);
                    break;
            }
            System.err.println(score);
            toolbar.sendEvents(score);
        }
    }
