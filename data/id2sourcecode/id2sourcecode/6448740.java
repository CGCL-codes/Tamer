    protected void setPatchNum(int patchNum) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.PROGRAM_CHANGE, getChannel() - 1, patchNum, 0);
            send(msg);
        } catch (InvalidMidiDataException e) {
            Logger.reportStatus(e);
        }
    }
