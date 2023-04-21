    public boolean process(boolean force, ClassType recorderClass, ClassObjectReference recorderClassObject, HashMap<String, String> methodDatabase) {
        Field compactIndexField = recorderClass.fieldByName("_compactIndex");
        if (null == compactIndexField) {
            throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._compactIndex field.");
        }
        Value compactIndexValue = recorderClass.getValue(compactIndexField);
        if (!(compactIndexValue instanceof IntegerValue)) {
            throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._compactIndex.");
        }
        IntegerValue compactIndex = (IntegerValue) compactIndexValue;
        int syncPointsInCompactList = compactIndex.intValue() / SyncPointBuffer.COMPACT_DEBUG_RECORD_SIZE;
        _record.setProperty("numSyncPointsInCompactList", syncPointsInCompactList);
        _record.setProperty("numTotalCompactSyncPoints", _record.getTotalNumCompactSyncPoints() + syncPointsInCompactList);
        _writer.println("// Total number of compact sync points so far: " + _record.getProperty("numTotalCompactSyncPoints"));
        try {
            if ((!force) && (null != recorderClassObject.owningThread())) {
                return false;
            } else {
                Field compactArrayField = recorderClass.fieldByName("_compactSyncPoints");
                if (null == compactArrayField) {
                    throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._compactSyncPoints field.");
                }
                Value compactArrayValue = recorderClass.getValue(compactArrayField);
                if (!(compactArrayValue instanceof ArrayReference)) {
                    throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._compactSyncPoints.");
                }
                ArrayReference compactArray = (ArrayReference) compactArrayValue;
                List<Value> compactValues = compactArray.getValues();
                for (int i = 0; i < compactIndex.intValue(); i += SyncPointBuffer.COMPACT_DEBUG_RECORD_SIZE) {
                    long tid = ((LongValue) compactValues.get(i)).longValue();
                    long code = ((LongValue) compactValues.get(i + 1)).longValue();
                    long classIndex = ((LongValue) compactValues.get(i + 2)).longValue();
                    long methodAndPC = ((LongValue) compactValues.get(i + 3)).longValue();
                    long oid = ((LongValue) compactValues.get(i + 4)).longValue();
                    processSyncPoint(tid, code, classIndex, methodAndPC, oid, methodDatabase);
                }
                _writer.flush();
            }
        } catch (IncompatibleThreadStateException e) {
            throw new Error("Could not access edu.rice.cs.cunit.SyncPointBuffer class lock.", e);
        }
        try {
            recorderClass.setValue(compactIndexField, _record.getVM().mirrorOf(0));
        } catch (InvalidTypeException e) {
            throw new Error("Could not set edu.rice.cs.cunit._compactIndex to 0.", e);
        } catch (ClassNotLoadedException e) {
            throw new Error("Could not set edu.rice.cs.cunit._compactIndex to 0.", e);
        }
        for (CompactThreadInfo cti : _threads.values()) {
            _writer.print("// Thread ");
            _writer.println(cti.toString());
        }
        _waitGraph = new CompactWaitGraph(_threads, _writer);
        _writer.flush();
        return true;
    }
