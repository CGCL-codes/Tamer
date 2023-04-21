    public void moveObject(Map sourceUri, Map targetUri, boolean overwrite) throws NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException, WriteEntryDataException {
        Map sourceMap = new HashMap();
        String sourceInfo = objectInfo(sourceUri, sourceMap);
        if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
            throw new NoSuchObjectException("The source object does not exist");
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_DIRECTORY)) {
            if (!isLibraryFolder(getLeafBinder(sourceMap))) {
                throw new KablinkFileSystemException("Can not move or rename binder that is not library folder", true);
            } else {
                Map targetMap = new HashMap();
                String targetInfo = objectInfo(targetUri, targetMap);
                if (getParentBinderPath(sourceMap).equals(getParentBinderPath(targetMap))) {
                    if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
                        throw new AlreadyExistsException("Cannot rename folder: An object with the same target name already exists");
                    } else {
                        renameFolder(sourceUri, sourceMap, targetUri, targetMap);
                    }
                } else {
                    if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
                        throw new AlreadyExistsException("Cannot move folder: An object with the same target name already exists");
                    } else {
                        moveFolder(sourceUri, sourceMap, targetUri, targetMap);
                    }
                }
            }
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_VIRTUAL_HELP_FILE)) {
            throw new KablinkFileSystemException("Can not move or rename virtual help file", true);
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_FILE)) {
            Map targetMap = new HashMap();
            String targetInfo = objectInfo(targetUri, targetMap);
            if (getParentBinderPath(sourceMap).equals(getParentBinderPath(targetMap))) {
                if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
                    throw new AlreadyExistsException("Cannot rename file: An object with the same target name already exists");
                } else {
                    renameResource(sourceUri, sourceMap, targetUri, targetMap);
                }
            } else {
                if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
                    throw new AlreadyExistsException("Cannot move file: An object with the same target name already exists");
                } else {
                    moveResource(sourceUri, sourceMap, targetUri, targetMap);
                }
            }
        }
    }
