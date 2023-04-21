    public void macroCopy(String sourceUri, String targetUri, boolean overwrite, boolean recursive) throws ServiceAccessException, AccessDeniedException, ObjectNotFoundException, ObjectAlreadyExistsException {
        log.info("copying " + sourceUri + " to " + targetUri);
        try {
            File fromFile = getFile(sourceUri);
            File toFile = getFile(targetUri);
            if (toFile.exists() && !overwrite) {
                throw new ObjectAlreadyExistsException(targetUri);
            }
            if (!toFile.getParentFile().exists()) {
                throw new ObjectNotFoundException(toFile.getParentFile().toString());
            }
            if (fromFile.isDirectory() && !recursive) {
                if (!toFile.exists()) {
                    toFile.mkdirs();
                }
            } else {
                FileHelper.copyRec(fromFile, toFile);
            }
            File propertyFile = getPropertyFile(sourceUri);
            File destPropertyFile = getPropertyFile(targetUri);
            if (propertyFile.exists()) FileHelper.copy(propertyFile, destPropertyFile);
        } catch (FileNotFoundException e) {
            throw new ObjectNotFoundException(targetUri);
        } catch (IOException e) {
            throw new ServiceAccessException(service, e);
        } catch (SecurityException e) {
            throw new AccessDeniedException(targetUri, e.getMessage(), "/actions/write");
        }
    }
