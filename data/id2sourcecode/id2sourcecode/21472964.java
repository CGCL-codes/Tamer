    private boolean checkUser(CallContext context, boolean writeRequired) throws CMISFileShareException {
        if (context == null) {
            throw new CMISFileShareException(EnumServiceException.PERMISSION_DENIED, "No user context.", 0);
        }
        Boolean readOnly = fUserMap.get(context.getUser());
        if (readOnly == null) {
            throw new CMISFileShareException(EnumServiceException.PERMISSION_DENIED, "Unknown user.", 0);
        }
        if (readOnly.booleanValue() && writeRequired) {
            throw new CMISFileShareException(EnumServiceException.PERMISSION_DENIED, "No write permission.", 0);
        }
        return readOnly.booleanValue();
    }
