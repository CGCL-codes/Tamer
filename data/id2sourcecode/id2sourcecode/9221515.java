    EventThread(VirtualMachine vm, String[] excludes, PrintWriter writer) {
        super("event-handler");
        this.vm = vm;
        this.excludes = excludes;
        this.writer = writer;
    }
