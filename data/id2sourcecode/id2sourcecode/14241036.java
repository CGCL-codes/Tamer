    public void storePatch(Patch p, int bankNum, int patchNum) {
        byte[] newsysex = new byte[264];
        System.arraycopy(((Patch) p).sysex, 0, newsysex, 0, 264);
        newsysex[4] = (byte) (0x70 + getChannel() - 1);
        newsysex[5] = (byte) (0x20);
        newsysex[6] = (byte) computeSlot(bankNum, patchNum);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        try {
            send(newsysex);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        setBankNum(bankNum);
        setPatchNum(patchNum);
    }
