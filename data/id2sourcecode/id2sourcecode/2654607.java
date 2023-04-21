    public void postUpdate(WriteObjectQuery writeQuery) throws DatabaseException, OptimisticLockException {
        if (isReadOnly()) {
            return;
        }
        if (!isAttributeValueInstantiated(writeQuery.getObject())) {
            return;
        }
        Object objects = getRealCollectionAttributeValueFromObject(writeQuery.getObject(), writeQuery.getSession());
        Object currentObjectsInDB = readPrivateOwnedForObject(writeQuery);
        if (currentObjectsInDB == null) {
            currentObjectsInDB = getContainerPolicy().containerInstance(1);
        }
        compareObjectsAndWrite(currentObjectsInDB, objects, writeQuery);
    }
