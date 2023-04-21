    protected void rebuildIndexForObject(LocalObjectContainer container, final int objectId) throws FieldIndexException {
        StatefulBuffer writer = container.readStatefulBufferById(container.systemTransaction(), objectId);
        if (writer != null) {
            rebuildIndexForWriter(container, writer, objectId);
        }
    }
