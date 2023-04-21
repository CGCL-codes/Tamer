    public void setAcl(AccessControlList acl) {
        this.acl = acl;
        if (acl != null) {
            if (AccessControlList.REST_CANNED_PRIVATE.equals(acl)) {
                addMetadata(Constants.REST_HEADER_PREFIX + "acl", "private");
            } else if (AccessControlList.REST_CANNED_PUBLIC_READ.equals(acl)) {
                addMetadata(Constants.REST_HEADER_PREFIX + "acl", "public-read");
            } else if (AccessControlList.REST_CANNED_PUBLIC_READ_WRITE.equals(acl)) {
                addMetadata(Constants.REST_HEADER_PREFIX + "acl", "public-read-write");
            } else if (AccessControlList.REST_CANNED_AUTHENTICATED_READ.equals(acl)) {
                addMetadata(Constants.REST_HEADER_PREFIX + "acl", "authenticated-read");
            } else {
            }
        }
    }
