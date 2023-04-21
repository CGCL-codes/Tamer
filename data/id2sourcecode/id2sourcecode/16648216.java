    protected Map createObjectImpl(String bucketName, String objectKey, String contentType, RequestEntity requestEntity, Map metadata, AccessControlList acl) throws S3ServiceException {
        if (metadata == null) {
            metadata = new HashMap();
        } else {
            metadata = new HashMap(metadata);
        }
        if (contentType != null) {
            metadata.put("Content-Type", contentType);
        } else {
            metadata.put("Content-Type", Mimetypes.MIMETYPE_OCTET_STREAM);
        }
        boolean putNonStandardAcl = false;
        if (acl != null) {
            if (AccessControlList.REST_CANNED_PRIVATE.equals(acl)) {
                metadata.put(Constants.REST_HEADER_PREFIX + "acl", "private");
            } else if (AccessControlList.REST_CANNED_PUBLIC_READ.equals(acl)) {
                metadata.put(Constants.REST_HEADER_PREFIX + "acl", "public-read");
            } else if (AccessControlList.REST_CANNED_PUBLIC_READ_WRITE.equals(acl)) {
                metadata.put(Constants.REST_HEADER_PREFIX + "acl", "public-read-write");
            } else if (AccessControlList.REST_CANNED_AUTHENTICATED_READ.equals(acl)) {
                metadata.put(Constants.REST_HEADER_PREFIX + "acl", "authenticated-read");
            } else {
                putNonStandardAcl = true;
            }
        }
        log.debug("Creating object bucketName=" + bucketName + ", objectKey=" + objectKey + "." + " Content-Type=" + metadata.get("Content-Type") + " Including data? " + (requestEntity != null) + " Metadata: " + metadata + " ACL: " + acl);
        HttpMethodAndByteCount methodAndByteCount = performRestPut(bucketName, objectKey, metadata, null, requestEntity);
        HttpMethodBase httpMethod = methodAndByteCount.getHttpMethod();
        Map map = new HashMap();
        map.putAll(metadata);
        map.putAll(convertHeadersToMap(httpMethod.getResponseHeaders()));
        map.put(S3Object.METADATA_HEADER_CONTENT_LENGTH, String.valueOf(methodAndByteCount.getByteCount()));
        map = ServiceUtils.cleanRestMetadataMap(map);
        if (putNonStandardAcl) {
            log.debug("Creating object with a non-canned ACL using REST, so an extra ACL Put is required");
            putAclImpl(bucketName, objectKey, acl);
        }
        return map;
    }
