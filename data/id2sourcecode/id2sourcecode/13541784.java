        public void send(MidiMessage message, long timeStamp) {
            if (closed) {
                throw new IllegalStateException();
            }
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
                synth.send(shortMessage.getChannel(), shortMessage.getCommand(), shortMessage.getData1(), shortMessage.getData2());
            }
        }
