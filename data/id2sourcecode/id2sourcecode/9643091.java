    private void prepare(WSSecHeader secHeader) throws WSSecurityException {
        KerberosSession krbSession = KerberosSessionCache.getInstance().getCurrentSession();
        if (krbSession == null) {
            krbSession = KerberosSessionCache.getInstance().getSession(user, servicePrincipalName);
        }
        KerberosSessionCache.getInstance().removeCurrentSession();
        secRef = new SecurityTokenReference(document);
        strUri = "STRId-" + secRef.hashCode();
        secRef.setID(strUri);
        byte[] tokenData = null;
        if (krbSession == null) {
            try {
                KerberosTicket tgt = getTicketGrantingTicket();
                tokenData = getServiceTicketData(servicePrincipalName);
                sessionKey = getSessionKey(tgt);
                MessageDigest digest = MessageDigest.getInstance("SHA");
                digest.update(tokenData);
                byte[] thumbPrintBytes = digest.digest();
                krbSession = new KerberosSession(null, Base64.encode(thumbPrintBytes), sessionKey);
                krbSession.setClientPrincipalName(user);
                krbSession.setServerPrincipalName(servicePrincipalName);
                KerberosSessionCache.getInstance().addSession(krbSession);
            } catch (LoginException e) {
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, "kerberosLoginFailed", new Object[] { e.getMessage() });
            } catch (GSSException e) {
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, "kerberosSTReqFailed", new Object[] { servicePrincipalName, e.getMessage() });
            } catch (Exception e) {
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, "kerberosSTReqFailed", new Object[] { servicePrincipalName, e.getMessage() });
            }
            if (tokenData == null) {
                throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, "kerberosSTReqFailed", new Object[] { servicePrincipalName, "Check service principal exists in KDC" });
            }
            tokenUri = "KerbTokenId-" + tokenData.hashCode();
        } else {
            keyIdentifierType = WSConstants.THUMBPRINT_IDENTIFIER;
        }
        wsDocInfo = new WSDocInfo(document.hashCode());
        switch(keyIdentifierType) {
            case WSConstants.BST_DIRECT_REFERENCE:
                Reference ref = new Reference(document);
                ref.setURI("#" + tokenUri);
                bstToken = new KerberosSecurity(document);
                ((KerberosSecurity) bstToken).setKerberosToken(tokenData);
                ref.setValueType(bstToken.getValueType());
                secRef.setReference(ref);
                bstToken.setID(tokenUri);
                wsDocInfo.setBst(bstToken.getElement());
                break;
            case WSConstants.THUMBPRINT_IDENTIFIER:
                secRef.setKerberosIdentifierThumb(krbSession);
                sessionKey = krbSession.getSessionKey();
                break;
            default:
                throw new WSSecurityException(WSSecurityException.FAILURE, "unsupportedKeyId");
        }
    }
