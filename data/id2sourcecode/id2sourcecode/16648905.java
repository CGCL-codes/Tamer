    private OSCPacket getOSCPacket(ShortMessage midiMsg) {
        Object packetContents[] = new Object[2];
        if (midiMsg.getCommand() == 144) {
            packetContents[0] = new Integer(midiMsg.getData1());
            packetContents[1] = new Float(midiMsg.getData2() / 127.0);
        } else if (midiMsg.getCommand() == 128) {
            packetContents[0] = new Integer(midiMsg.getData1());
            packetContents[1] = new Float(0.0);
        } else {
            setError("Unrecognized command: " + midiMsg.getCommand());
        }
        return new OSCMessage("/mas/channel" + (midiMsg.getChannel() + 1), packetContents);
    }
