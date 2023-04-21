    private final Object readIndexEntryForRebuild(StatefulBuffer writer, ObjectHeader oh) {
        ClassMetadata classMetadata = oh.classMetadata();
        if (classMetadata == null) {
            return defaultValueForFieldType();
        }
        ObjectIdContextImpl context = new ObjectIdContextImpl(writer.transaction(), writer, oh, writer.getID());
        if (!classMetadata.seekToField(context, this)) {
            return defaultValueForFieldType();
        }
        try {
            return readIndexEntry(context);
        } catch (CorruptionException exc) {
            throw new FieldIndexException(exc, this);
        }
    }
