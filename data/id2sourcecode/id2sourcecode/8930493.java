    public void run() {
        Logger.global.finer("Bank initializing ");
        Guard[] guards = new Guard[ident.length + 1];
        java.lang.System.arraycopy(ident, 0, guards, 0, ident.length);
        guards[ident.length] = ready;
        Alternative alt = new Alternative(guards);
        int choose = alt.select();
        while (choose != ident.length) {
            currentChannel = choose;
            ident[choose].write(ident((ATMSpec) ident[choose].read()));
            Logger.global.finer("Bank choosing");
            choose = alt.select();
            Logger.global.finest("Bank has chosen");
        }
        Logger.global.fine("Bank avaiting ready");
        ready.read();
        ready();
        setup(new Data(new java.util.Date().toString()));
        upAndRunning();
    }
