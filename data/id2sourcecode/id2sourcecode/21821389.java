    public void controlChange(ShortMessage message) {
        synchronized (this.lockMeta) {
            if (this.currentMusic == null) return;
            if (message.getCommand() != 176) return;
            this.channels[message.getChannel()].controlChange(7, (int) (((this.musicVolume) / 100.0) * 127.0));
        }
    }
