    private void upAndRunning() {
        Logger.global.finer("Bank entered upAndRunning ");
        for (int i = 0; i < machines.length; i++) Logger.global.finest(machines[i].toString());
        Guard[] guards = new Guard[auth.length + debit.length];
        java.lang.System.arraycopy(auth, 0, guards, 0, auth.length);
        java.lang.System.arraycopy(debit, 0, guards, auth.length, debit.length);
        Alternative alt = new Alternative(guards);
        while (true) {
            if (hasRequests()) {
                Logger.global.finest("Bank has requests");
                int chan = getRequest();
                ATMSpec addr = machines[chan];
                Boolean ok = answers[chan];
                Logger.global.finest("Bank sends result");
                result(addr, ok);
            } else {
                Logger.global.finest("Bank reads");
                int chan = alt.select();
                if (chan < auth.length) {
                    Logger.global.fine("Bank read auth " + chan);
                    getAccount(new AccountID(16));
                    auth[chan].write(auth((IDSpec) auth[chan].read(), (PINSpec) auth[chan].read()));
                } else {
                    chan -= auth.length;
                    Logger.global.fine("Bank read debit " + chan);
                    debit((ATMSpec) debit[chan].read(), (IDSpec) debit[chan].read(), (SUMSpec) debit[chan].read());
                }
            }
        }
    }
