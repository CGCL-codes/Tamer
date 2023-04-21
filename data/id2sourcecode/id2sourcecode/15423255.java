    public void ready() {
        Logger.global.fine("ATM sends ready " + this);
        c_ready.write(null);
    }
