    protected void rebuildIndexForWriter(LocalObjectContainer stream, StatefulBuffer writer, final int objectId) {
        ObjectHeader oh = new ObjectHeader(stream, writer);
        Object obj = readIndexEntryForRebuild(writer, oh);
        addIndexEntry(stream.systemTransaction(), objectId, obj);
    }
