    public void copyObject(Map sourceUri, Map targetUri, boolean overwrite, boolean recursive) throws NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        Map sourceMap = new HashMap();
        String sourceInfo = objectInfo(sourceUri, sourceMap);
        if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
            throw new NoSuchObjectException("The source object does not exist");
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_DIRECTORY)) {
            throw new TypeMismatchException("Directory can not be copied");
        } else if (sourceInfo.equals(CrossContextConstants.OBJECT_INFO_VIRTUAL_HELP_FILE)) {
            throw new TypeMismatchException("Virtual help file can not be copied");
        }
        Map targetMap = new HashMap();
        String targetInfo = objectInfo(targetUri, targetMap);
        if (!targetInfo.equals(CrossContextConstants.OBJECT_INFO_NON_EXISTING)) {
            if (targetInfo.equals(CrossContextConstants.OBJECT_INFO_DIRECTORY)) {
                throw new TypeMismatchException("The source and target types do not match");
            } else if (targetInfo.equals(CrossContextConstants.OBJECT_INFO_VIRTUAL_HELP_FILE)) {
                throw new TypeMismatchException("Can not copy a file into a binder that is not a library folder");
            } else {
                copyFile(getFolderEntry(sourceMap), getFolderEntry(targetMap), getLastElemName(targetMap));
            }
        } else {
            Binder targetParentBinder = getParentBinder(targetMap);
            if (targetParentBinder == null) throw new NoSuchObjectException("The target's parent binder does not exist");
            if (!isLibraryFolder(targetParentBinder)) throw new TypeMismatchException("It is not allowed to copy a file into a binder that is not a library folder");
            copyFile(getFolderEntry(sourceMap), (Folder) targetParentBinder, getLastElemName(targetMap));
        }
    }
