    private void loadSynchronizationPoints() {
        if (_vm == null || _eventThread == null || !_eventThread.isConnected()) {
            return;
        }
        _writer.println("// Note: Attempting to load sync points");
        _vm.suspend();
        try {
            List<ReferenceType> classes = _vm.classesByName("edu.rice.cs.cunit.SyncPointBuffer");
            ClassType bufferClass = null;
            for (ReferenceType cl : classes) {
                if (cl.name().equals("edu.rice.cs.cunit.SyncPointBuffer")) {
                    if (cl instanceof ClassType) {
                        bufferClass = (ClassType) cl;
                        break;
                    }
                }
            }
            if (null == bufferClass) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer class.");
            }
            Field replayArrayField = bufferClass.fieldByName("_replaySyncPoints");
            if (null == replayArrayField) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._replaySyncPoints field.");
            }
            Value replayArrayValue = bufferClass.getValue(replayArrayField);
            if (!(replayArrayValue instanceof ArrayReference)) {
                throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._replaySyncPoints.");
            }
            ArrayReference replayArray = (ArrayReference) replayArrayValue;
            ArrayList<LongValue> values = new ArrayList<LongValue>();
            boolean eof = false;
            while (!eof && (values.size() < SyncPointBuffer.REPLAY_SIZE)) {
                eof = !readSyncPoint(values, true);
            }
            _writer.println("// Note: read " + (values.size() / 2) + " sync points from trace file (buffer size = " + (SyncPointBuffer.REPLAY_SIZE / 2) + ")");
            _writer.flush();
            while (values.size() < SyncPointBuffer.REPLAY_SIZE) {
                LongValue zero = _vm.mirrorOf((long) 0);
                values.add(zero);
            }
            try {
                replayArray.setValues(values);
            } catch (InvalidTypeException e) {
                throw new Error("Could not set SyncPointBuffer._replaySyncPoints contents.");
            } catch (ClassNotLoadedException e) {
                throw new Error("Could not set SyncPointBuffer._replaySyncPoints contents.");
            }
        } finally {
            _writer.flush();
            _vm.resume();
        }
    }
