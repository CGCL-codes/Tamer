    public NtlmUserAccount authenticate(byte[] material) {
        try {
            Type3Message type3Message = new Type3Message(material);
            if (type3Message.getFlag(_NTLMSSP_NEGOTIATE_EXTENDED_SESSION_SECURITY) && (type3Message.getNTResponse().length == 24)) {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] bytes = new byte[16];
                System.arraycopy(_serverChallenge, 0, bytes, 0, 8);
                System.arraycopy(type3Message.getLMResponse(), 0, bytes, 8, 8);
                messageDigest.update(bytes);
                _serverChallenge = messageDigest.digest();
            }
            return NetlogonUtil.logon(type3Message.getDomain(), type3Message.getUser(), type3Message.getWorkstation(), _serverChallenge, type3Message.getNTResponse(), type3Message.getLMResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
