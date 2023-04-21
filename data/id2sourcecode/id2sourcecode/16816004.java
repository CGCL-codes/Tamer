    EventThread(VirtualMachine vm, PrintWriter writer, IReplayAdapter ra) {
        super("event-handler");
        _vm = vm;
        _writer = writer;
        _ra = ra;
    }
