    public void moveObject(Map sourceUri, Map targetUri, boolean overwrite) throws NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        Map sourceMap = new HashMap();
        String sourceInfo = objectInfo(sourceUri, sourceMap);
        if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
            throw new NoSuchObjectException("The source object does not exist");
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_DIRECTORY)) {
            if (!isLibraryFolder(getLeafBinder(sourceMap))) {
                throw new SiteScapeFileSystemException("Can not move or rename binder that is not library folder", true);
            }
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_VIRTUAL_HELP_FILE)) {
            throw new SiteScapeFileSystemException("Can not move or rename virtual help file", true);
        }
        Map targetMap = new HashMap();
        String targetInfo = objectInfo(targetUri, targetMap);
        if (!getParentBinderPath(sourceMap).equals(getParentBinderPath(targetMap))) {
            throw new SiteScapeFileSystemException("Cannot move: It is not allowed", true);
        }
        if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
            throw new AlreadyExistsException("Cannot rename: An object with the name you specified already exists");
        }
        if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_DIRECTORY)) renameFolder(sourceUri, sourceMap, targetUri, targetMap); else renameResource(sourceUri, sourceMap, targetUri, targetMap);
    }
