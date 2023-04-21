    void generateTrace(PrintWriter writer, boolean closeWriter) {
        _vm.setDebugTraceMode(_debugTraceMode);
        _eventThread = new EventThread(_vm, writer, new IViewAdapter() {

            public void updateThreadList() {
                if (_view != null) {
                    _view.updateList(getThreadList());
                }
            }

            public void updateSynchronizationPoints(boolean force, String desc) {
                if (_view != null) {
                    processSynchronizationPoints(force, desc);
                    _view.updateList(getThreadList());
                }
            }

            public void setMainEntered() {
                Record.this.setMainEntered();
            }

            public void vmStartEvent() {
                enableRecording(_includeInitSyncPoints);
            }

            public void updateSynchronizationPointsImmediately(boolean force, String desc) {
                if (_view != null) {
                    processSynchronizationPointsImmediately(force, desc);
                }
            }
        });
        _eventThread.setEventRequests();
        _eventThread.start();
        redirectOutput();
        _vm.resume();
        _startTime = System.currentTimeMillis();
        if (_auto) {
            Thread updaterThread = new Thread(new Runnable() {

                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(_autoDelay);
                            if (!isUpdateDelayed()) {
                                processSynchronizationPoints(false, "timed");
                                if (_view != null) {
                                    _view.updateList(getThreadList());
                                }
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            updaterThread.setDaemon(true);
            updaterThread.start();
        }
        try {
            _eventThread.join();
            _errThread.join();
            _outThread.join();
        } catch (InterruptedException exc) {
        }
        if (_view != null) {
            _view.setProjectInWindowTitle("[stopped]");
        }
        if (_processObjectSyncPoints) {
            writer.println("// Total number of object sync points: " + getTotalNumSyncPoints());
        }
        writer.println("// Total number of compact sync points: " + getTotalNumCompactSyncPoints());
        writer.println("0 " + SyncPointBuffer.SP.END.intValue() + " // End of schedule");
        writer.flush();
        if (closeWriter) {
            writer.close();
        }
        _methodDatabase = null;
    }
