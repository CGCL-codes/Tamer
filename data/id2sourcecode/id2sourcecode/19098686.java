    public void onCondition(ShortMessage mes, long timeS) {
        int channel = mes.getChannel();
        int bank = mes.getData2();
        ShortMessage res = new ShortMessage();
        try {
            res.setMessage(ShortMessage.PROGRAM_CHANGE, channel, bank, -1);
        } catch (InvalidMidiDataException e) {
            throw new Error(e);
        }
        this.sendToAll(res, timeS);
    }
