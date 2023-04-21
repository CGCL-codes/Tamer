    protected void setBankNum(int bankNum) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.CONTROL_CHANGE, getChannel() - 1, 0x20, bankNum);
            send(msg);
        } catch (InvalidMidiDataException e) {
            ErrorMsg.reportStatus(e);
        }
    }
