    public void notify(IServiceCall call, byte channel) {
        Notify notify = new Notify();
        notify.setCall(call);
        getChannel(channel).write(notify);
    }
