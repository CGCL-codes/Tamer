    public boolean consume(MidiMessage mess, long stamp) {
        if (!(mess instanceof ShortMessage)) {
            return true;
        }
        ShortMessage smsg = (ShortMessage) mess;
        if (learning) {
            System.out.println("LEARNING: ch cmd data1 data2: " + smsg.getChannel() + " " + smsg.getCommand() + " " + smsg.getData1() + " " + smsg.getData2());
            if (smsg.getCommand() == ShortMessage.NOTE_OFF) {
                return true;
            }
            lastMessage = smsg;
            return true;
        } else {
            long key = MidiHashUtil.hashValue((ShortMessage) mess);
            ControlMapper mapper = map.get(key);
            if (mapper == null) {
                return false;
            }
            mapper.send(smsg, stamp);
            return true;
        }
    }
