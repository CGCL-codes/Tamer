    public void macroMove(String sourceUri, String targetUri, boolean overwrite) throws ServiceAccessException, AccessDeniedException, ObjectNotFoundException, ObjectAlreadyExistsException {
        log.info("moving " + sourceUri + " to " + targetUri);
        try {
            File fromFile = getFile(sourceUri);
            File toFile = getFile(targetUri);
            if (toFile.exists() && !overwrite) {
                throw new ObjectAlreadyExistsException(targetUri);
            }
            if (!toFile.getParentFile().exists()) {
                throw new ObjectNotFoundException(toFile.getParentFile().toString());
            }
            if (Config.isFrontEnd()) {
                Config.getFileInfosys().rename(getDiskFilePath(sourceUri), getDiskFilePath(targetUri));
            }
            renameOrMove(fromFile, toFile);
            File propertyFile = getPropertyFile(sourceUri);
            File destPropertyFile = getPropertyFile(targetUri);
            renameOrMove(propertyFile, destPropertyFile);
            File lockFile = getLockFile(sourceUri);
            File destLockFile = getLockFile(targetUri);
            renameOrMove(lockFile, destLockFile);
            File errFile = getErrFile(sourceUri);
            File destErrFile = getErrFile(targetUri);
            renameOrMove(errFile, destErrFile);
        } catch (FileNotFoundException e) {
            throw new ObjectNotFoundException(targetUri);
        } catch (IOException e) {
            throw new ServiceAccessException(service, e);
        } catch (SecurityException e) {
            throw new AccessDeniedException(targetUri, e.getMessage(), "/actions/write");
        }
    }
