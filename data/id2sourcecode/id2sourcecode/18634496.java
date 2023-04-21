        public void send(MidiMessage message, long timeStamp) throws IllegalStateException {
            if (message instanceof ShortMessage) {
                ShortMessage smessage = (ShortMessage) message;
                switch(message.getStatus()) {
                    case ShortMessage.NOTE_ON:
                        int velocity = smessage.getData2();
                        if (velocity > 0) channels[smessage.getChannel()].noteOn(smessage.getData1(), smessage.getData2()); else channels[smessage.getChannel()].noteOff(smessage.getData1());
                        break;
                    case ShortMessage.CONTROL_CHANGE:
                        channels[smessage.getChannel()].controlChange(smessage.getData1(), smessage.getData2());
                        break;
                    default:
                        System.out.println("Unhandled message: " + message.getStatus());
                        break;
                }
            }
        }
