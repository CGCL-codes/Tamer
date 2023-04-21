    public void macroCopy(String sourceUri, String targetUri, boolean overwrite, boolean recursive) throws ServiceAccessException, AccessDeniedException, ObjectNotFoundException, ObjectAlreadyExistsException, ObjectLockedException {
        Map sm = null;
        Map tm = null;
        try {
            sm = parseUri(sourceUri);
        } catch (ZoneMismatchException e) {
            throw new AccessDeniedException(sourceUri, e.getLocalizedMessage(), "/actions/write");
        }
        try {
            tm = parseUri(targetUri);
        } catch (ZoneMismatchException e) {
            throw new AccessDeniedException(targetUri, e.getLocalizedMessage(), "/actions/write");
        }
        try {
            client.copyObject(sourceUri, sm, targetUri, tm, overwrite, recursive);
        } catch (NoAccessException e) {
            throw new AccessDeniedException(targetUri, e.getLocalizedMessage(), "/actions/write");
        } catch (CCClientException e) {
            throw new ServiceAccessException(service, e.getLocalizedMessage(), e.isWarning());
        } catch (NoSuchObjectException e) {
            throw new ObjectNotFoundException(sourceUri);
        } catch (AlreadyExistsException e) {
            throw new ObjectAlreadyExistsException(targetUri);
        } catch (TypeMismatchException e) {
            throw new ServiceAccessException(service, e.getLocalizedMessage());
        }
    }
