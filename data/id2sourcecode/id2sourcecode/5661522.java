    public void send(MidiMessage message, long timeStamp) {
        if (this.useMIDIPageChanging && this.pageChangeConfigMode == false) {
            if (message instanceof ShortMessage) {
                ShortMessage msg = (ShortMessage) message;
                int velocity = msg.getData1();
                if (msg.getCommand() == ShortMessage.NOTE_ON && velocity > 0) {
                    int channel = msg.getChannel();
                    int note = msg.getData1();
                    for (int i = 0; i < this.midiPageChangeRules.size(); i++) {
                        MIDIPageChangeRule mpcr = this.midiPageChangeRules.get(i);
                        if (mpcr.checkRule(note, channel) == true) {
                            int switchToPageIndex = mpcr.getPageIndex();
                            Page page = this.pages.get(switchToPageIndex);
                            this.switchPage(page, switchToPageIndex, true);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < this.numPages; i++) {
            this.pages.get(i).send(message, timeStamp);
        }
    }
