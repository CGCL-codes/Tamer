            int getChannel(MidiEvent e) {
                MidiMessage m = e.getMessage();
                if (m instanceof ShortMessage) {
                    return ((ShortMessage) m).getChannel();
                } else {
                    return -1;
                }
            }
