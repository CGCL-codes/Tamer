    public void requestPatchDump(int bankNum, int patchNum) {
        byte chan = (byte) (0x70 + getChannel() - 1);
        byte slot = (byte) computeSlot(bankNum, patchNum);
        byte sysex[] = { (byte) 0xF0, 0x44, 0, 0, chan, 0x10, slot, chan, 0x31, (byte) 0xF7 };
        send(sysex);
    }
