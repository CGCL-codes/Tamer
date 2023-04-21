    public void setPatchNum(int patchNum) {
        this.curPatch = patchNum;
        try {
            send(0xC0 + (getChannel() - 1), computeSlot(curBank, curPatch));
        } catch (Exception e) {
        }
        ;
    }
