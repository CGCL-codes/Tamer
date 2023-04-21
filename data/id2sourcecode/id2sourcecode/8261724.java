    public void invoke(IServiceCall call, byte channel) {
        Invoke invoke = new Invoke();
        invoke.setCall(call);
        synchronized (invokeId) {
            invoke.setInvokeId(invokeId);
            if (call instanceof IPendingServiceCall) {
                synchronized (pendingCalls) {
                    pendingCalls.put(invokeId, (IPendingServiceCall) call);
                }
            }
            invokeId += 1;
        }
        getChannel(channel).write(invoke);
    }
