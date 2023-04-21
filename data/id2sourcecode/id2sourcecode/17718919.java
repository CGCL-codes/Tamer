    public void requestPatchDump(int bankNum, int patchNum) {
        int location = patchNum;
        int opcode = QSConstants.OPCODE_MIDI_USER_MIX_DUMP_REQ;
        send(sysexRequestDump.toSysexMessage(getChannel(), new SysexHandler.NameValue("opcode", opcode), new SysexHandler.NameValue("patchNum", location)));
    }
