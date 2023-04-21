    protected void setBankNum(int bankNum) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.CONTROL_CHANGE, getChannel() - 1, 0x00, bankNum / 128);
            send(msg);
            msg.setMessage(ShortMessage.CONTROL_CHANGE, getChannel() - 1, 0x20, bankNum % 128);
            send(msg);
        } catch (InvalidMidiDataException e) {
            Logger.reportStatus(e);
        }
    }
