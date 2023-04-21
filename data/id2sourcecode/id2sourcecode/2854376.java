    public void transport(MidiMessage msg, long timestamp) {
        if (isChannel(msg)) {
            int chan = ChannelMsg.getChannel(msg);
            decoders[chan].decode(getCommand(msg), getData1(msg), getData2(msg));
        }
        input.transport(msg, timestamp);
    }
