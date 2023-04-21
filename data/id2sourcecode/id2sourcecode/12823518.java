        public Message(boolean input, MidiMessage message) {
            this.input = input;
            int status = message.getStatus();
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
                if (MessageUtils.isChannelStatus(status)) {
                    this.status = String.valueOf(status & 0xf0);
                    this.channel = String.valueOf(shortMessage.getChannel());
                } else {
                    this.status = String.valueOf(status);
                    this.channel = "-";
                }
                this.data1 = String.valueOf(shortMessage.getData1());
                this.data2 = String.valueOf(shortMessage.getData2());
            } else {
                this.status = String.valueOf(status);
                this.data1 = "-";
                this.data2 = "-";
                this.channel = "-";
            }
            this.length = message.getLength();
            this.description = buildDescription();
            this.color = buildColor(status);
        }
