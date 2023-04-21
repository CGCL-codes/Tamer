    void generateTrace(PrintWriter writer) {
        vm.setDebugTraceMode(debugTraceMode);
        EventThread eventThread = new EventThread(vm, excludes, writer);
        eventThread.setEventRequests(watchFields);
        eventThread.start();
        redirectOutput();
        vm.resume();
        try {
            eventThread.join();
            errThread.join();
            outThread.join();
        } catch (InterruptedException exc) {
        }
        writer.close();
    }
