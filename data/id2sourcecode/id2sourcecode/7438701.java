    protected CARS_ActionContext createActionContext(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws IOException, ParseException, NoSuchAlgorithmException {
        CARS_ActionContext ac = null;
        String auth = pRequest.getHeader("Authorization");
        if (auth != null) {
            String username = null, password = null;
            if (auth.toUpperCase().startsWith("BASIC ") && gALLOWED_AUTH.contains(AUTH_TYPE.BASIC)) {
                final String userpassEncoded = auth.substring(6);
                final String encoding = new String(BASE64Decoder.decodeBuffer(userpassEncoded));
                final int in = encoding.indexOf(':');
                username = encoding.substring(0, in);
                password = encoding.substring(in + 1);
                ac = CARS_ActionContext.createActionContext(username, password.toCharArray());
            } else if (auth.toUpperCase().startsWith("DIGEST ") && gALLOWED_AUTH.contains(AUTH_TYPE.DIGEST)) {
                final HashMap<String, String> authMap = CARS_Digest.parseAuthenticationString(auth);
                final MessageDigest md = MessageDigest.getInstance("MD5");
                username = authMap.get("username");
                final String uri = authMap.get("uri");
                final byte[] md5 = md.digest((pRequest.getMethod() + ":" + uri).getBytes());
                final String ha2 = StringUtil.bytesToHexString(md5);
                password = authMap.get("response") + "\n:" + authMap.get("nonce") + ":" + authMap.get("nc") + ":" + authMap.get("cnonce") + ":" + authMap.get("qop") + ":" + ha2;
                ac = CARS_ActionContext.createActionContext(username, (AUTH_TYPE.DIGEST.toString() + "|" + password).toCharArray());
            } else if (auth.toUpperCase().startsWith("GOOGLELOGIN AUTH=")) {
                final String key = auth.substring(17);
                ac = CARS_ActionContext.createActionContext(key);
            } else {
                pResponse.setHeader(WWW_AUTH, getCurrentRealm());
                pResponse.sendError(pResponse.SC_UNAUTHORIZED);
            }
        }
        if (auth == null) {
            final String q = pRequest.getQueryString();
            if (q != null) {
                final int ix = q.indexOf("GOOGLELOGIN_AUTH=");
                if (ix != -1) {
                    auth = q.substring(ix + "GOOGLELOGIN_AUTH=".length());
                    if (auth.indexOf('&') != -1) {
                        auth = auth.substring(0, auth.indexOf('&'));
                    }
                    ac = CARS_ActionContext.createActionContext(auth);
                }
            }
        }
        final String pathInfo = pRequest.getPathInfo();
        if ((auth == null) && (pathInfo != null)) {
            if ("/accounts/login".equals(pathInfo)) {
                pResponse.setHeader(WWW_AUTH, getCurrentRealm());
                pResponse.sendError(pResponse.SC_UNAUTHORIZED);
            } else {
                if (pathInfo.startsWith("/accounts/")) {
                    ac = CARS_ActionContext.createActionContext("anonymous", "anonymous".toCharArray());
                } else {
                    pResponse.setHeader(WWW_AUTH, getCurrentRealm());
                    pResponse.sendError(pResponse.SC_UNAUTHORIZED);
                }
            }
        } else {
            if (pathInfo == null) {
                return null;
            }
        }
        if (ac != null) {
            ac.setIfModifiedSince(pRequest.getHeader("If-Modified-Since"));
            ac.setIfNoneMatch(pRequest.getHeader("If-None-Match"));
            ac.setRemoteHost(pRequest.getRemoteHost());
            ac.setUserAgent(pRequest.getHeader("User-Agent"));
            ac.setReferer(pRequest.getHeader("Referer"));
            ac.setServerPort(pRequest.getServerPort());
            final String range = pRequest.getHeader("Range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    String brange = range.substring("bytes=".length());
                    int ix = brange.indexOf('-');
                    if (ix != -1) {
                        String end = brange.substring(ix + 1);
                        if ("".equals(end)) {
                            ac.setRange(Long.parseLong(brange.substring(0, ix)), Long.MAX_VALUE);
                        } else {
                            ac.setRange(Long.parseLong(brange.substring(0, ix)), Long.parseLong(brange.substring(ix + 1)));
                        }
                    }
                }
            }
        }
        return ac;
    }
