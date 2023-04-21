    protected void connect() throws IOException {
        NtlmPasswordAuthentication ntlmPasswordAuthentication = new NtlmPasswordAuthentication(null, _ntlmServiceAccount.getAccount(), _ntlmServiceAccount.getPassword());
        String endpoint = "ncacn_np:" + _domainController + "[\\PIPE\\NETLOGON]";
        _handle = DcerpcHandle.getHandle(endpoint, ntlmPasswordAuthentication);
        _handle.bind();
        byte[] clientChallenge = new byte[8];
        _secureRandom.nextBytes(clientChallenge);
        NetrServerReqChallenge netrServerReqChallenge = new NetrServerReqChallenge(_domainControllerName, _ntlmServiceAccount.getComputerName(), clientChallenge, new byte[8]);
        _handle.sendrecv(netrServerReqChallenge);
        MD4 md4 = new MD4();
        md4.update(_ntlmServiceAccount.getPassword().getBytes("UTF-16LE"));
        byte[] sessionKey = computeSessionKey(md4.digest(), clientChallenge, netrServerReqChallenge.getServerChallenge());
        byte[] clientCredential = computeNetlogonCredential(clientChallenge, sessionKey);
        NetrServerAuthenticate3 netrServerAuthenticate3 = new NetrServerAuthenticate3(_domainControllerName, _ntlmServiceAccount.getAccountName(), 2, _ntlmServiceAccount.getComputerName(), clientCredential, new byte[8], 0xFFFFFFFF);
        _handle.sendrecv(netrServerAuthenticate3);
        byte[] serverCredential = computeNetlogonCredential(netrServerReqChallenge.getServerChallenge(), sessionKey);
        if (!Arrays.equals(serverCredential, netrServerAuthenticate3.getServerCredential())) {
            System.out.println("Session key negotiation failed");
            return;
        }
        _clientCredential = clientCredential;
        _sessionKey = sessionKey;
    }
