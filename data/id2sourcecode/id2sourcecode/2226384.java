    public static String paramString(MidiMessage midiMessage) {
        String result = null;
        if (midiMessage instanceof ShortMessage) {
            ShortMessage msg = (ShortMessage) midiMessage;
            int channel = msg.getChannel();
            int command = msg.getCommand();
            int data1 = msg.getData1();
            int data2 = msg.getData2();
            result = "channel=" + (channel + 1) + ",event=" + getChannelMessage(command, data1) + ",data1=" + (command == ShortMessage.PROGRAM_CHANGE ? data1 + " " + MidiConstants.getInstrumentName(data1) : String.valueOf(data1)) + ",data2=" + data2;
        } else if (midiMessage instanceof SysexMessage) {
            SysexMessage msg = (SysexMessage) midiMessage;
            byte[] data = msg.getData();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(StringUtil.toHex2(data[i]));
                sb.append(" ");
            }
            result = "channel=n/a" + ",event=SYSX" + ",data1=" + sb + ",data2=";
        } else if (midiMessage instanceof MetaMessage) {
            MetaMessage msg = (MetaMessage) midiMessage;
            int type = msg.getType();
            byte[] data = msg.getData();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(StringUtil.toHex2(data[i]));
                sb.append(" ");
            }
            result = "channel=n/a" + ",event=meta" + ",data1=" + type + ",data2=" + sb;
        }
        return result;
    }
