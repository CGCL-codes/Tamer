    @Override
    public ApplicationState getState(int eid, boolean sendState) {
        stateLock.lock();
        if (eid == -1 || eid > lastEid) return new CounterState();
        byte[] b = new byte[4];
        byte[] d = null;
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((counter >>> offset) & 0xFF);
        }
        stateLock.unlock();
        d = md.digest(b);
        return new CounterState(lastEid, (sendState ? b : null), d);
    }
