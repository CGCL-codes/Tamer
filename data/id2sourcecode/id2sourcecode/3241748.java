    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (receivers.isEmpty()) {
            return;
        }
        if (message instanceof ShortMessage) {
            ShortMessage root = (ShortMessage) message;
            if (root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF) {
                try {
                    ShortMessage third = new ShortMessage();
                    third.setMessage(root.getCommand(), root.getChannel(), root.getData1() + thirdInterval, root.getData2());
                    ShortMessage fifth = new ShortMessage();
                    fifth.setMessage(root.getCommand(), root.getChannel(), root.getData1() + fifthInterval, root.getData2());
                    for (Receiver receiver : receivers) {
                        receiver.send(root, timeStamp);
                        receiver.send(third, timeStamp);
                        receiver.send(fifth, timeStamp);
                    }
                    return;
                } catch (InvalidMidiDataException ex) {
                }
            }
        }
        for (Receiver receiver : receivers) {
            receiver.send(message, timeStamp);
        }
    }
