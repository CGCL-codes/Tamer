    @SuppressWarnings("unchecked")
    public void methodPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream out = null;
        try {
            S3ObjectRequest or;
            try {
                or = S3ObjectRequest.create(req, resolvedHost(), (Authenticator) getWebApplicationContext().getBean(BEAN_AUTHENTICATOR));
            } catch (InvalidAccessKeyIdException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "InvalidAccessKeyId");
                return;
            } catch (InvalidSecurityException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "InvalidSecurity");
                return;
            } catch (RequestTimeTooSkewedException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "RequestTimeTooSkewed");
                return;
            } catch (SignatureDoesNotMatchException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "SignatureDoesNotMatch");
                return;
            } catch (AuthenticatorException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "InvalidSecurity");
                return;
            }
            logger.debug("S3ObjectRequest: " + or);
            CanonicalUser requestor = or.getRequestor();
            if (or.getKey() != null) {
                String value;
                long contentLength;
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                DigestOutputStream digestOutputStream = null;
                S3Object oldS3Object = null;
                S3Object s3Object;
                StorageService storageService;
                Bucket bucket;
                String bucketName = or.getBucket();
                String key = or.getKey();
                if (!isValidKey(key)) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "KeyTooLong");
                    return;
                }
                storageService = (StorageService) getWebApplicationContext().getBean(BEAN_STORAGE_SERVICE);
                if (req.getParameter(PARAMETER_ACL) != null) {
                    Acp acp;
                    CanonicalUser owner;
                    s3Object = storageService.load(bucketName, key);
                    if (s3Object == null) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NoSuchKey");
                        return;
                    }
                    acp = s3Object.getAcp();
                    try {
                        acp.canWrite(requestor);
                    } catch (AccessControlException e) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessDenied");
                        return;
                    }
                    owner = acp.getOwner();
                    try {
                        acp = Acp.decode(req.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "MalformedACLError");
                        return;
                    }
                    acp.setOwner(owner);
                    s3Object.setAcp(acp);
                    storageService.store(s3Object);
                } else {
                    try {
                        bucket = storageService.loadBucket(bucketName);
                        bucket.canWrite(requestor);
                    } catch (AccessControlException e) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessDenied");
                        return;
                    } catch (DataAccessException e) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NoSuchBucket");
                        return;
                    }
                    try {
                        oldS3Object = storageService.load(bucket.getName(), key);
                    } catch (DataRetrievalFailureException e) {
                    }
                    try {
                        s3Object = storageService.createS3Object(bucket, key, requestor);
                    } catch (DataAccessException e) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NoSuchBucket");
                        return;
                    }
                    out = s3Object.getOutputStream();
                    digestOutputStream = new DigestOutputStream(out, messageDigest);
                    value = req.getHeader("Content-Length");
                    if (value == null) {
                        resp.sendError(HttpServletResponse.SC_LENGTH_REQUIRED, "MissingContentLength");
                        return;
                    }
                    contentLength = Long.valueOf(value).longValue();
                    if (contentLength > 5368709120L) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "EntityTooLarge");
                        return;
                    }
                    long written = 0;
                    int count;
                    byte[] b = new byte[4096];
                    ServletInputStream in = req.getInputStream();
                    while (((count = in.read(b, 0, b.length)) > 0) && (written < contentLength)) {
                        digestOutputStream.write(b, 0, count);
                        written += count;
                    }
                    digestOutputStream.flush();
                    if (written != contentLength) {
                        if (out != null) {
                            out.close();
                            out = null;
                        }
                        if (digestOutputStream != null) {
                            digestOutputStream.close();
                            digestOutputStream = null;
                        }
                        storageService.remove(s3Object);
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "IncompleteBody");
                        return;
                    }
                    s3Object.setContentDisposition(req.getHeader("Content-Disposition"));
                    s3Object.setContentLength(contentLength);
                    s3Object.setContentMD5(req.getHeader("Content-MD5"));
                    value = req.getContentType();
                    logger.debug("Put - Content-Type: " + value);
                    if (value == null) {
                        value = S3Object.DEFAULT_CONTENT_TYPE;
                    }
                    s3Object.setContentType(value);
                    logger.debug("Put - get content-type: " + s3Object.getContentType());
                    s3Object.setLastModified(System.currentTimeMillis());
                    int prefixLength = HEADER_PREFIX_USER_META.length();
                    String name;
                    for (Enumeration headerNames = req.getHeaderNames(); headerNames.hasMoreElements(); ) {
                        String headerName = (String) headerNames.nextElement();
                        if (headerName.startsWith(HEADER_PREFIX_USER_META)) {
                            name = headerName.substring(prefixLength).toLowerCase();
                            for (Enumeration headers = req.getHeaders(headerName); headers.hasMoreElements(); ) {
                                value = (String) headers.nextElement();
                                s3Object.addMetadata(name, value);
                            }
                        }
                    }
                    value = new String(Hex.encodeHex(digestOutputStream.getMessageDigest().digest()));
                    resp.setHeader("ETag", value);
                    s3Object.setETag(value);
                    grantCannedAccessPolicies(req, s3Object.getAcp(), requestor);
                    if (oldS3Object != null) {
                        storageService.remove(oldS3Object);
                    }
                    storageService.store(s3Object);
                }
            } else if (or.getBucket() != null) {
                StorageService storageService;
                Bucket bucket;
                storageService = (StorageService) getWebApplicationContext().getBean(BEAN_STORAGE_SERVICE);
                if (req.getParameter(PARAMETER_ACL) != null) {
                    Acp acp;
                    CanonicalUser owner;
                    logger.debug("User is providing new ACP for bucket " + or.getBucket());
                    try {
                        bucket = storageService.loadBucket(or.getBucket());
                    } catch (DataAccessException e) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NoSuchBucket");
                        return;
                    }
                    acp = bucket.getAcp();
                    try {
                        acp.canWrite(requestor);
                    } catch (AccessControlException e) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessDenied");
                        return;
                    }
                    owner = acp.getOwner();
                    try {
                        acp = Acp.decode(req.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "MalformedACLError");
                        return;
                    }
                    acp.setOwner(owner);
                    bucket.setAcp(acp);
                    logger.debug("Saving bucket ACP");
                    logger.debug("ACP: " + Acp.encode(bucket.getAcp()));
                    storageService.storeBucket(bucket);
                } else {
                    String bucketName = or.getBucket();
                    if (!isValidBucketName(bucketName)) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "InvalidBucketName");
                        return;
                    }
                    try {
                        bucket = storageService.createBucket(bucketName, requestor);
                    } catch (BucketAlreadyExistsException e) {
                        resp.sendError(HttpServletResponse.SC_CONFLICT, "BucketAlreadyExists");
                        return;
                    }
                    grantCannedAccessPolicies(req, bucket.getAcp(), requestor);
                    storageService.storeBucket(bucket);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("Unable to use MD5", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "InternalError");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
        }
    }
