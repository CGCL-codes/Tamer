    public boolean process(boolean force, ClassType recorderClass, ClassObjectReference recorderClassObject, HashMap<String, String> methodDatabase) {
        Field indexField = recorderClass.fieldByName("_index");
        if (null == indexField) {
            throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._index field.");
        }
        Value indexValue = recorderClass.getValue(indexField);
        if (!(indexValue instanceof IntegerValue)) {
            throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._index.");
        }
        IntegerValue index = (IntegerValue) indexValue;
        _record.setProperty("numSyncPointsInList", index.intValue());
        _record.setProperty("numTotalSyncPoints", _record.getTotalNumSyncPoints() + index.intValue());
        _writer.println("// Total number of object sync points so far: " + _record.getProperty("numTotalSyncPoints"));
        try {
            if ((!force) && (null != recorderClassObject.owningThread())) {
                return false;
            } else {
                Field arrayField = recorderClass.fieldByName("_syncPoints");
                if (null == arrayField) {
                    throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._syncPoints field.");
                }
                Value arrayValue = recorderClass.getValue(arrayField);
                if (!(arrayValue instanceof ArrayReference)) {
                    throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._syncPoints.");
                }
                ArrayReference array = (ArrayReference) arrayValue;
                List<Value> values = array.getValues();
                for (int i = 0; i < index.intValue(); ++i) {
                    processSyncPointListEntry(values.get(i));
                }
            }
        } catch (IncompatibleThreadStateException e) {
            throw new Error("Could not access edu.rice.cs.cunit.SyncPointBuffer class lock.", e);
        }
        try {
            recorderClass.setValue(indexField, _record.getVM().mirrorOf(0));
        } catch (InvalidTypeException e) {
            throw new Error("Could not set edu.rice.cs.cunit._index to 0.", e);
        } catch (ClassNotLoadedException e) {
            throw new Error("Could not set edu.rice.cs.cunit._index to 0.", e);
        }
        _waitGraph = new WaitGraph(_threads, _writer);
        return true;
    }
