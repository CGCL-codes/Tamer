    public void processRead(int r, PID source) {
        if ((read > r) || (write > r)) sendNackRead(source, r); else {
            read = r;
            sendAckRead(source, r);
        }
    }
