    private void authenticateUserMD5(String handle, String passwd) throws ConnectionException {
        handle = addDomain(handle);
        String[] args = { "MD5", "I", handle };
        Response res = getRes(Command.USR, args);
        if (Command.XFR.equals(res.getCommand())) {
            handleXFR(res);
            res = getRes(Command.USR, args);
        }
        if (!Command.USR.equals(res.getCommand())) {
            throw new ConnectionException("Protocol error during authentication");
        }
        String hashRes = res.getArg(2) + passwd;
        byte[] digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(hashRes.getBytes("US-ASCII"));
        } catch (NoSuchAlgorithmException ex) {
            throw new ConnectionException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new ConnectionException(ex.getMessage());
        }
        args[1] = "S";
        args[2] = Util.bytesToString(digest, 0, 16);
        res = getRes(Command.USR, args);
        if (res == null) {
            throw new ConnectionException("Protocol error during authentication");
        }
        if (!Command.USR.equals(res.getCommand())) {
            throw new ConnectionException("Protocol error during authentication");
        }
        m_commonName = res.getArg(2);
        m_handle = handle;
        m_passwd = passwd;
    }
