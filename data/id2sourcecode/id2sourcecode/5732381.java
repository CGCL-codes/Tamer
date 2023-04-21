    public void setBankNum(int bankNum) {
        this.curBank = bankNum;
        try {
            send(0xC0 + (getChannel() - 1), computeSlot(curBank, curPatch));
        } catch (Exception e) {
        }
    }
