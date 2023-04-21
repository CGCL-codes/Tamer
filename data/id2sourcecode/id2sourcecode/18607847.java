    protected void register() throws IOException {
        if (interest != key.interestOps()) {
            if (Event.LOG) {
                log((interest == READ ? "read" : "write") + " prereg " + interest + " " + key.interestOps() + " " + key.readyOps(), DEBUG);
            }
            key = channel.register(key.selector(), interest, this);
            if (Event.LOG) {
                log((interest == READ ? "read" : "write") + " postreg " + interest + " " + key.interestOps() + " " + key.readyOps(), DEBUG);
            }
        }
        key.selector().wakeup();
        if (Event.LOG) {
            log((interest == READ ? "read" : "write") + " wakeup", DEBUG);
        }
    }
