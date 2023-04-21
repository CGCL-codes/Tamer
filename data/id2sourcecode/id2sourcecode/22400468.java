    private static void xmitVoiceOperatorState(Driver drv, int st) {
        drv.send(VoiceOPstate.toSysexMessage(0x10 + drv.getChannel(), new SysexHandler.NameValue("value", st)));
    }
