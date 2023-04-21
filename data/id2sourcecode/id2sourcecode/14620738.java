    void generateTrace(PrintWriter writer, boolean closeWriter) {
        _vm.setDebugTraceMode(_debugTraceMode);
        _eventThread = new EventThread(_vm, writer, new IReplayAdapter() {

            public void loadSynchronizationPoints() {
                Replay.this.loadSynchronizationPoints();
            }

            public void processMessage() {
                Replay.this.processMessage();
            }

            public void enableReplay() {
                Replay.this.enableReplay();
            }
        });
        _eventThread.setEventRequests();
        _eventThread.start();
        redirectOutput();
        _vm.resume();
        try {
            _eventThread.join();
            _errThread.join();
            _outThread.join();
        } catch (InterruptedException exc) {
        }
        long numReadBack = _numTotalSyncPointsRead;
        writer.println("// Note: Number of sync points read back from trace file = " + numReadBack);
        while (readSyncPoint(null, false)) {
        }
        writer.println("// Note: Number of sync points NOT read back from trace file before application exited = " + (_numTotalSyncPointsRead - numReadBack));
        writer.flush();
        if (closeWriter) {
            writer.close();
        }
    }
