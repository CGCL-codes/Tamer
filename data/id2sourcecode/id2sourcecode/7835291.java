    private String checkAuthenticatedCredentials(HttpServletRequest request, HttpServletResponse response) throws InternalServerErrorException {
        String authHeaderParams = request.getHeader(HttpConstant.HEADER_AUTHORIZATION);
        if (logger.isFineEnabled()) {
            logger.fine("Authorization header included with value: " + authHeaderParams);
        }
        final int digestParamsStart = 6;
        if (authHeaderParams.length() > digestParamsStart) {
            authHeaderParams = authHeaderParams.substring(digestParamsStart);
        }
        String username = null;
        String password = null;
        String realm = null;
        String nonce = null;
        String uri = null;
        String cnonce = null;
        String nc = null;
        String qop = null;
        String resp = null;
        String opaque = null;
        for (String param : authHeaderParams.split(",")) {
            int i = param.indexOf('=');
            if (i > 0 && i < (param.length() - 1)) {
                String paramName = param.substring(0, i).trim();
                String paramValue = param.substring(i + 1).trim();
                if (paramName.equals("username")) {
                    if (paramValue.length() > 2) {
                        username = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Username param with value " + username);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("nonce")) {
                    if (paramValue.length() > 2) {
                        nonce = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Nonce param with value " + nonce);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("cnonce")) {
                    if (paramValue.length() > 2) {
                        cnonce = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("CNonce param with value " + cnonce);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("realm")) {
                    if (paramValue.length() > 2) {
                        realm = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Realm param with value " + realm);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("nc")) {
                    nc = paramValue;
                    if (logger.isFineEnabled()) {
                        logger.fine("Nonce-count param with value " + nc);
                    }
                } else if (paramName.equals("response")) {
                    if (paramValue.length() > 2) {
                        resp = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Response param with value " + resp);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("uri")) {
                    if (paramValue.length() > 2) {
                        uri = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Digest uri param with value " + uri);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("opaque")) {
                    if (paramValue.length() > 2) {
                        opaque = paramValue.substring(1, paramValue.length() - 1);
                        if (logger.isFineEnabled()) {
                            logger.fine("Opaque param with value " + opaque);
                        }
                    } else {
                        if (logger.isFineEnabled()) {
                            logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                        }
                    }
                } else if (paramName.equals("qop")) {
                    if (paramValue.charAt(0) == '"') {
                        if (paramValue.length() > 2) {
                            qop = paramValue.substring(1, paramValue.length() - 1);
                        } else {
                            if (logger.isFineEnabled()) {
                                logger.fine("Ignoring invalid param " + paramName + " value " + paramValue);
                            }
                        }
                    } else {
                        qop = paramValue;
                    }
                    if (logger.isFineEnabled()) {
                        logger.fine("Qop param with value " + qop);
                    }
                }
            } else {
                if (logger.isFineEnabled()) {
                    logger.fine("Ignoring invalid param " + param);
                }
            }
        }
        if (username == null || realm == null || nonce == null || cnonce == null || nc == null || uri == null || resp == null || opaque == null) {
            logger.severe("A required parameter is missing in the challenge response");
            return null;
        }
        if (challengeParamGenerator.getNonce(opaque).equals(nonce)) {
            if (logger.isFineEnabled()) logger.fine("Nonce provided matches the one generated using opaque as seed");
        } else {
            if (logger.isFineEnabled()) logger.fine("Authentication failed, nonce provided doesn't match the one generated using opaque as seed");
            return null;
        }
        if (!qop.equals("auth")) {
            if (logger.isFineEnabled()) logger.fine("Authentication failed, qop value " + qop + " unsupported");
            return null;
        }
        UserProfile userProfile = getUserProfileControlSbb().find(username);
        if (userProfile == null) {
            if (logger.isFineEnabled()) logger.fine("Authentication failed, profile not found for user " + username);
            return null;
        } else {
            password = userProfile.getPassword();
        }
        final String digest = new RFC2617AuthQopDigest(username, realm, password, nonce, nc, cnonce, request.getMethod().toUpperCase(), uri).digest();
        if (digest != null && digest.equals(resp)) {
            if (logger.isFineEnabled()) logger.fine("authentication response is matching");
            String params = "cnonce=\"" + cnonce + "\", nc=" + nc + ", qop=" + qop + ", rspauth=\"" + digest + "\"";
            response.addHeader("Authentication-Info", params);
            return username;
        } else {
            if (logger.isFineEnabled()) logger.fine("authentication response digest received (" + resp + ") didn't match the one calculated (" + digest + ")");
            return null;
        }
    }
