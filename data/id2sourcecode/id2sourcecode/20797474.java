    protected void rebuildIndexForObject(LocalObjectContainer stream, final ClassMetadata classMetadata, final int objectId) throws FieldIndexException {
        StatefulBuffer writer = stream.readStatefulBufferById(stream.systemTransaction(), objectId);
        if (writer != null) {
            rebuildIndexForWriter(stream, writer, objectId);
        } else {
            if (Deploy.debug) {
                throw new Db4oException("Unexpected null object for ID");
            }
        }
    }
