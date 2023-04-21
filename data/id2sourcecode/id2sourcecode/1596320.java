    public void send(MidiMessage message, long timeStamp) {
        synchronized (control_mutex) {
            if (!open) throw new IllegalStateException("Receiver is not open");
        }
        if (timeStamp != -1) {
            synchronized (control_mutex) {
                mainmixer.activity();
                while (midimessages.get(timeStamp) != null) timeStamp++;
                if (message instanceof ShortMessage && (((ShortMessage) message).getChannel() > 0xF)) {
                    midimessages.put(timeStamp, message.clone());
                } else {
                    midimessages.put(timeStamp, message.getMessage());
                }
            }
        } else {
            mainmixer.processMessage(message);
        }
    }
