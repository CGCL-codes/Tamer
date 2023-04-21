    public void processWrite(int r, PID source, Transportable estimateFromW) {
        if ((read > r) || (write > r)) sendNackWrite(source, r); else {
            write = r;
            estimate = estimateFromW;
            sendAckWrite(source, r);
        }
    }
