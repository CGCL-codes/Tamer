    public Request buildRequestWithAuthorizationHeader(ResponseEvent event, String password) throws TransactionUnavailableException {
        Request request = event.getClientTransaction().getRequest();
        Response response = event.getResponse();
        if (request == null) {
            if (log.isDebugEnabled()) {
                log.debug("The request that caused the 407 could not be retrieved.");
            }
            return null;
        } else {
            CSeqHeader cseqHeader = (CSeqHeader) request.getHeader(CSeqHeader.NAME);
            FromHeader fromHeaderReq = (FromHeader) request.getHeader(FromHeader.NAME);
            Address fromAddressReq = fromHeaderReq.getAddress();
            ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
            Address toAddress = toHeader.getAddress();
            Request newRequest = null;
            String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
            try {
                newRequest = buildInvite(fromAddressReq, toAddress, null, cseqHeader.getSequenceNumber() + 1, callId);
            } catch (ParseException parseExc) {
                parseExc.printStackTrace();
            } catch (InvalidArgumentException invaliArgExc) {
                invaliArgExc.printStackTrace();
            }
            WWWAuthenticateHeader wwwAuthenticateHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
            ProxyAuthenticateHeader proxyAuthenticateHeader = (ProxyAuthenticateHeader) response.getHeader(ProxyAuthenticateHeader.NAME);
            String realm = null;
            String nonce = null;
            if (wwwAuthenticateHeader != null) {
                if (log.isDebugEnabled()) {
                    log.debug("wwwAuthenticateHeader found!");
                }
                realm = wwwAuthenticateHeader.getRealm();
                nonce = wwwAuthenticateHeader.getNonce();
            } else if (proxyAuthenticateHeader != null) {
                if (log.isDebugEnabled()) {
                    log.debug("ProxyAuthenticateHeader found!");
                }
                realm = proxyAuthenticateHeader.getRealm();
                nonce = proxyAuthenticateHeader.getNonce();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Neither a ProxyAuthenticateHeader or AuthorizationHeader found!");
                }
                return null;
            }
            final String method = cseqHeader.getMethod();
            final FromHeader fromHeader = ((FromHeader) response.getHeader(FromHeader.NAME));
            Address address = fromHeader.getAddress();
            String fromHost = null;
            String fromUser = null;
            int fromPort = 0;
            String toHost = null;
            String toUser = null;
            int toPort = 0;
            SipURI fromSipURI = null;
            SipURI toSipURI = null;
            try {
                fromSipURI = convertAddressToSipURI(address);
                toSipURI = convertAddressToSipURI(toAddress);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            fromHost = fromSipURI.getHost();
            fromUser = fromSipURI.getUser();
            fromPort = fromSipURI.getPort();
            toHost = toSipURI.getHost();
            toUser = toSipURI.getUser();
            toPort = toSipURI.getPort();
            if (fromPort != -1) {
                fromHost += ":" + fromPort;
            }
            SipURI uri = null;
            try {
                uri = addressFactory.createSipURI(toUser, toHost);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String A1 = fromUser + ":" + realm + ":" + password;
            String A2 = method.toUpperCase() + ":" + uri.toString();
            byte mdbytes[] = md5.digest(A1.getBytes());
            String HA1 = toHexString(mdbytes);
            mdbytes = md5.digest(A2.getBytes());
            String HA2 = toHexString(mdbytes);
            String KD = HA1 + ":" + nonce + ":" + HA2;
            mdbytes = md5.digest(KD.getBytes());
            if (wwwAuthenticateHeader != null) {
                AuthorizationHeader ah = null;
                try {
                    ah = headerFactory.createAuthorizationHeader("Digest");
                    ah.setUsername(fromUser);
                    ah.setRealm(realm);
                    ah.setAlgorithm("MD5");
                    ah.setURI(uri);
                    ah.setNonce(nonce);
                    ah.setResponse(toHexString(mdbytes));
                    newRequest.setHeader(ah);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ProxyAuthorizationHeader pah = null;
                try {
                    pah = headerFactory.createProxyAuthorizationHeader("Digest");
                    pah.setUsername(fromUser);
                    pah.setRealm(realm);
                    pah.setAlgorithm("MD5");
                    pah.setURI(uri);
                    pah.setNonce(nonce);
                    pah.setResponse(toHexString(mdbytes));
                    newRequest.setHeader(pah);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("********* New Request *******************");
            System.out.println(newRequest);
            return newRequest;
        }
    }
