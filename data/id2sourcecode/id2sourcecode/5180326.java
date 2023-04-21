    EventThread(VirtualMachine vm, PrintWriter writer, IViewAdapter viewAdapter) {
        super("event-handler");
        _vm = vm;
        _writer = writer;
        _viewAdapter = viewAdapter;
    }
