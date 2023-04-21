    public Request createNewRequest(SipStack sipStack, Request originalRequest, Response response, int count) {
        Exception ex = null;
        try {
            Request newRequest = (Request) originalRequest.clone();
            CSeqHeader cseqHeader = newRequest.getCSeqHeader();
            cseqHeader.setSequenceNumber(cseqHeader.getSequenceNumber() + 1);
            ProxyAuthenticateHeader proxyAuthHeader = (ProxyAuthenticateHeader) response.getHeader(ProxyAuthenticateHeader.NAME);
            WWWAuthenticateHeader wwwAuthenticateHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
            cseqHeader = response.getCSeqHeader();
            method = cseqHeader.getMethod();
            uri = originalRequest.getRequestURI().toString();
            String opaque = null;
            if (proxyAuthHeader == null) {
                if (wwwAuthenticateHeader == null) {
                    if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                        Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "ERROR: No ProxyAuthenticate header " + "or WWWAuthenticateHeader " + "in the response!");
                    }
                    return null;
                }
                algorithm = wwwAuthenticateHeader.getAlgorithm();
                nonce = wwwAuthenticateHeader.getNonce();
                realm = wwwAuthenticateHeader.getRealm();
                if (realm == null) {
                    if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                        Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "ERROR: the realm is not part " + "of the 401 response!");
                    }
                    return null;
                }
                qop = wwwAuthenticateHeader.getParameter("qop");
                opaque = wwwAuthenticateHeader.getParameter("opaque");
            } else {
                algorithm = proxyAuthHeader.getAlgorithm();
                nonce = proxyAuthHeader.getNonce();
                realm = proxyAuthHeader.getRealm();
                if (realm == null) {
                    if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                        Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "ERROR: the realm is not part " + "of the 407 response!");
                    }
                    return null;
                }
                qop = proxyAuthHeader.getParameter("qop");
                opaque = proxyAuthHeader.getParameter("opaque");
            }
            if (algorithm == null) {
                algorithm = MD5;
            }
            if (!algorithm.equalsIgnoreCase(MD5) && !algorithm.equalsIgnoreCase(MD5_SESS)) {
                if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                    Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "Algorithm parameter is wrong: " + algorithm);
                }
                return null;
            }
            Credentials credentials = getCredentials(realm);
            if (credentials == null) {
                if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                    Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "ERROR: unable to retrieve " + "the credentials from RMS!");
                }
                return null;
            }
            if (nonce == null) {
                nonce = "";
            }
            String digestEntityBody = null;
            if (qop != null) {
                cnonce = toHexString(Utils.digest(("" + System.currentTimeMillis() + ":ETag:" + credentials.getPassword()).getBytes()));
                if (qop.equalsIgnoreCase("auth-int")) {
                    String entityBody = new String(originalRequest.getRawContent());
                    if (entityBody == null) {
                        entityBody = "";
                    }
                    digestEntityBody = toHexString(Utils.digest(entityBody.getBytes()));
                }
            }
            AuthenticationHeader header = null;
            if (proxyAuthHeader == null) {
                header = createAuthorizationHeader("Digest");
            } else {
                header = createProxyAuthorizationHeader("Digest");
            }
            header.setParameter("username", credentials.getUserName());
            header.setParameter("realm", realm);
            header.setParameter("uri", uri);
            header.setParameter("algorithm", algorithm);
            header.setParameter("nonce", nonce);
            if (qop != null) {
                Lexer qopLexer = new Lexer("qop", qop);
                boolean foundAuth = false;
                String currToken;
                while (qopLexer.lookAhead(0) != '\0') {
                    currToken = qopLexer.byteStringNoComma().toLowerCase();
                    if (currToken.equals("auth") || currToken.equals("auth-int")) {
                        foundAuth = true;
                        qop = currToken;
                        break;
                    }
                }
                if (!foundAuth) {
                    if (Logging.REPORT_LEVEL <= Logging.WARNING) {
                        Logging.report(Logging.WARNING, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "the digest response is null " + "for the Authorization header!");
                    }
                    return null;
                }
                header.setParameter("qop", qop);
                header.setParameter("cnonce", cnonce);
                String nonceCount = Integer.toHexString(count);
                nonceCountPar = "";
                int lengthNonceCount = nonceCount.length();
                if (lengthNonceCount < 8) {
                    for (int i = lengthNonceCount; i < 8; i++) {
                        nonceCountPar += "0";
                    }
                }
                nonceCountPar += nonceCount;
                header.setParameter("nc", nonceCountPar);
            }
            String digestResponse = generateResponse(credentials.getUserName(), credentials.getPassword(), digestEntityBody);
            if (digestResponse == null) {
                if (Logging.REPORT_LEVEL <= Logging.WARNING) {
                    Logging.report(Logging.WARNING, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "the digest response is null " + "for the Authorization header!");
                }
                return null;
            }
            header.setParameter("response", digestResponse);
            if (opaque != null) {
                header.setParameter("opaque", opaque);
            }
            newRequest.setHeader(header);
            return newRequest;
        } catch (ParseException pe) {
            ex = pe;
        } catch (javax.microedition.sip.SipException se) {
            ex = se;
        }
        if (ex != null) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "DigestClientAuthentication, " + "createNewRequest() " + "exception raised: " + ex.getMessage());
            }
        }
        return null;
    }
