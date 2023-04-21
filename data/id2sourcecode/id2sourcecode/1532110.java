        public void send(MidiMessage message, long timeStamp) {
            if (!isSelected()) {
                return;
            }
            ShortMessage msg = (ShortMessage) message;
            if (msg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int channel = msg.getChannel();
                int controller = msg.getData1();
                Logger.reportStatus("FaderReceiver: channel: " + channel + ", control: " + controller + ", value: " + msg.getData2());
                for (int i = 0; i < Constants.NUM_FADERS; i++) {
                    if ((appConfig.getFaderChannel(i) == channel) && (appConfig.getFaderControl(i) == controller)) {
                        faderMoved(i, msg.getData2());
                        break;
                    }
                }
            }
        }
