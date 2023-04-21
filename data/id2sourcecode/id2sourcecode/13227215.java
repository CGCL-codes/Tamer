    public void moveObject(Map sourceUri, Map targetUri, boolean overwrite) throws NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        if (isInternal(sourceUri)) ssfsInt.moveObject(sourceUri, targetUri, overwrite); else ssfsLib.moveObject(sourceUri, targetUri, overwrite);
    }
