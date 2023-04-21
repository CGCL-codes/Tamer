        @Override
        public void send(MidiMessage msg, long timeStamp) {
            int msgChannel = LAST_CHANNEL;
            if (!isOpen || !isReceiverOpen) {
                throw new IllegalStateException("ChannelDispatcher object is closed");
            }
            if (msg instanceof ShortMessage) msgChannel = ((ShortMessage) msg).getChannel();
            for (Transmitter t : channelTransmitters[msgChannel]) {
                Receiver rcv = t.getReceiver();
                if (rcv != null) rcv.send(msg, timeStamp);
            }
        }
