    public eu.mpower.framework.security.encryption.soap.GenerateCryptographicHashResponse generateCryptographicHash(eu.mpower.framework.security.encryption.soap.GenerateCryptographicHashRequest generateCryptographicHashRequest) {
        eu.mpower.framework.security.encryption.soap.GenerateCryptographicHashResponse response = new eu.mpower.framework.security.encryption.soap.GenerateCryptographicHashResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port2 = service2.getAuthorizationWServicePort();
            java.lang.String serviceID = "Encryption";
            java.lang.String methodName = "generateCryptographicHash";
            eu.mpower.framework.security.types.soap.OperationStatus result2 = port2.isAuthorized(generateCryptographicHashRequest.getSecurityToken(), serviceID, methodName);
            if (!result2.isBoolValue()) {
                status.setResult(0);
                status.setErrorCause(ErrorCodes.ENCRYPT_ERROR_AUTHORIZATION_REQUIRED.value());
                response.setStatus(status);
                return response;
            }
            eu.mpower.framework.security.encryption.soap.MessageDigest algorithm = generateCryptographicHashRequest.getGenerateCryptographicHash().getAlgorithm();
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(algorithm.value());
            messageDigest.update(generateCryptographicHashRequest.getGenerateCryptographicHash().getData());
            response.setData(messageDigest.digest());
            status.setResult(1);
            status.setMessageId(ErrorCodes.GENERATE_SIGNED_HASH_OK.ordinal());
            status.setErrorCause(ErrorCodes.GENERATE_SIGNED_HASH_OK.value());
        } catch (NoSuchAlgorithmException ex) {
            status.setResult(0);
            status.setMessageId(ErrorCodes.GENERATE_SIGNED_HASH_ERROR.ordinal());
            status.setErrorCause(ErrorCodes.GENERATE_SIGNED_HASH_ERROR.value());
        }
        status.setTimestamp(System.currentTimeMillis());
        response.setStatus(status);
        return response;
    }
