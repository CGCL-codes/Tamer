    public void copyObject(Map sourceUri, Map targetUri, boolean overwrite, boolean recursive) throws NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        if (isInternal(sourceUri)) ssfsInt.copyObject(sourceUri, targetUri, overwrite, recursive); else ssfsLib.copyObject(sourceUri, targetUri, overwrite, recursive);
    }
