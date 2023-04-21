    void sessionSetup(ServerMessageBlock andx, ServerMessageBlock andxResponse) throws SmbException {
        synchronized (transport()) {
            if (sessionSetup) {
                return;
            }
            transport.negotiate();
            if (DebugFile.trace) DebugFile.writeln("sessionSetup: accountName=" + auth.username + ",primaryDomain=" + auth.domain);
            SmbComSessionSetupAndX request = new SmbComSessionSetupAndX(this, andx);
            SmbComSessionSetupAndXResponse response = new SmbComSessionSetupAndXResponse(andxResponse);
            if (transport.isSignatureSetupRequired(auth)) {
                if (auth.hashesExternal && NtlmPasswordAuthentication.DEFAULT_PASSWORD != null) {
                    transport.getSmbSession(NtlmPasswordAuthentication.DEFAULT).getSmbTree(LOGON_SHARE, null).treeConnect(null, null);
                }
                request.digest = new SigningDigest(transport, auth);
            }
            request.auth = auth;
            transport.send(request, response);
            if (response.isLoggedInAsGuest && "GUEST".equals(auth.username)) {
                throw new SmbAuthException(NtStatus.NT_STATUS_LOGON_FAILURE);
            }
            uid = response.uid;
            sessionSetup = true;
            if (request.digest != null) {
                transport.digest = request.digest;
            }
        }
    }
