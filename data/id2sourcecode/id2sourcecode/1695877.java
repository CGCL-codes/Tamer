        @Override
        protected void filter(MidiMessage message, long timeStamp) {
            List<MidiMessage> messages = new LinkedList<MidiMessage>();
            ShortMessage third = null;
            ShortMessage fifth = null;
            if (message instanceof ShortMessage) {
                ShortMessage root = (ShortMessage) message;
                command = root.getCommand();
                channel = root.getChannel();
                data1 = root.getData1();
                data2 = root.getData2();
                if (root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF) {
                    try {
                        root.setMessage(command, channel, data1 + chordInversion.getRootInterval(), data2);
                        messages.add(root);
                    } catch (InvalidMidiDataException ex) {
                    }
                    third = new ShortMessage();
                    try {
                        third.setMessage(command, channel, data1 + chordType.getThirdInterval() + chordInversion.getThirdInterval(), data2);
                        messages.add(third);
                    } catch (InvalidMidiDataException ex) {
                    }
                    fifth = new ShortMessage();
                    try {
                        fifth.setMessage(command, channel, data1 + chordType.getFifthInterval() + chordInversion.getFifthInterval(), data2);
                        messages.add(fifth);
                    } catch (InvalidMidiDataException ex) {
                    }
                }
            }
            sendNow(messages);
        }
