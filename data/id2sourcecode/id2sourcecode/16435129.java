        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (isOpen == false || receiverClosed || transmitters.isEmpty()) {
                return;
            }
            ShortMessage third = null;
            ShortMessage fifth = null;
            if (message instanceof ShortMessage) {
                ShortMessage root = (ShortMessage) message;
                if (root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF) {
                    third = new ShortMessage();
                    try {
                        third.setMessage(root.getCommand(), root.getChannel(), root.getData1() + thirdInterval, root.getData2());
                    } catch (InvalidMidiDataException ex) {
                        third = null;
                    }
                    fifth = new ShortMessage();
                    try {
                        fifth.setMessage(root.getCommand(), root.getChannel(), root.getData1() + fifthInterval, root.getData2());
                    } catch (InvalidMidiDataException ex) {
                        fifth = null;
                    }
                }
            }
            for (Transmitter transmitter : transmitters) {
                Receiver receiver = transmitter.getReceiver();
                if (receiver != null) {
                    new Thread(new MessageSenderRunnable(receiver, message, timeStamp)).start();
                    if (third != null) {
                        new Thread(new MessageSenderRunnable(receiver, third, timeStamp)).start();
                    }
                    if (fifth != null) {
                        new Thread(new MessageSenderRunnable(receiver, fifth, timeStamp)).start();
                    }
                }
            }
        }
