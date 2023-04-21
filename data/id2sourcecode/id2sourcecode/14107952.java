    static void mergeObjectWithSnapshot(ObjEntity entity, DataObject object, DataRow snapshot) {
        int state = object.getPersistenceState();
        if (entity.isReadOnly() || state == PersistenceState.HOLLOW) {
            refreshObjectWithSnapshot(entity, object, snapshot, true);
        } else if (state != PersistenceState.COMMITTED || object.getDataContext().getChannel() instanceof ObjectContext) {
            forceMergeWithSnapshot(entity, object, snapshot);
        } else {
            refreshObjectWithSnapshot(entity, object, snapshot, false);
        }
    }
