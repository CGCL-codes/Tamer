    public void send(MidiMessage message, long timeStamp) {
        if (message.getStatus() >= ShortMessage.MIDI_TIME_CODE) return;
        if (isLinux) {
            if (message.getStatus() == ShortMessage.PITCH_BEND) {
                ShortMessage mess = (ShortMessage) message;
                short low = (byte) mess.getData1();
                short high = (byte) mess.getData2();
                int channel = mess.getChannel();
                low = (byte) mess.getData1();
                high = (byte) mess.getData2();
                high = (short) ((high + 64) & 0x007f);
                try {
                    mess.setMessage(ShortMessage.PITCH_BEND, channel, low, high);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
            }
        }
        chained.send(message, timeStamp);
        notifyListeners(message);
    }
