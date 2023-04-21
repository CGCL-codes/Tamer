            public int compare(MidiEvent e1, MidiEvent e2) {
                long t1 = e1.getTick();
                long t2 = e2.getTick();
                if (t1 - t2 != 0) {
                    return (int) (t1 - t2);
                } else {
                    int c1 = getChannel(e1);
                    int c2 = getChannel(e2);
                    return c1 - c2;
                }
            }
