            public void readEventPerformed(EEGReadEvent e) {
                values.addAll(e.getChannels());
            }
