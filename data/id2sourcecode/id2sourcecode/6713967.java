    public void writeIndexEntry(Context context, ByteArrayBuffer reader, Object obj) {
        MappedIDPair mappedIDs = (MappedIDPair) obj;
        _origHandler.writeIndexEntry(context, reader, new Integer(mappedIDs.orig()));
        _mappedHandler.writeIndexEntry(context, reader, new Integer(mappedIDs.mapped()));
    }
