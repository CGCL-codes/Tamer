    public void requestPatchDump(int bankNum, int patchNum) {
        int location = patchNum;
        int opcode = QSConstants.OPCODE_MIDI_USER_EFFECTS_DUMP_REQ;
        if (location > QSConstants.MAX_LOCATION_PROG) {
            location -= (QSConstants.MAX_LOCATION_PROG + 1);
            opcode = QSConstants.OPCODE_MIDI_EDIT_EFFECTS_DUMP_REQ;
        }
        send(sysexRequestDump.toSysexMessage(getChannel(), new SysexHandler.NameValue("opcode", opcode), new SysexHandler.NameValue("patchNum", location)));
    }
