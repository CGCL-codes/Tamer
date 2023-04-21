    public void send(MidiMessage mess, long arg1) {
        if (mess.getStatus() >= ShortMessage.MIDI_TIME_CODE) return;
        if (!(mess instanceof ShortMessage)) return;
        ShortMessage smsg = (ShortMessage) mess;
        switch(mode) {
            case LEARNING:
                System.out.println("ch cmd data1 data2: " + smsg.getChannel() + " " + smsg.getCommand() + " " + smsg.getData1() + " " + smsg.getData2());
                lastMessage = smsg;
                break;
            case ACTIVE:
                long key = MidiHashUtil.hashValue((ShortMessage) mess);
                ControlMapper mapper = map.get(key);
                if (mapper == null) return;
                mapper.send(smsg, arg1);
                break;
        }
    }
