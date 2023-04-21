    public String getCacheFileName(Proteu proteu) throws NoSuchAlgorithmException {
        String identity = file;
        if (isTypeURL()) {
            identity = identity.concat(proteu.getRequestHead().getString("URL"));
        }
        if (isTypeGet()) {
            identity = identity.concat(proteu.getRequestGet().getInFormat("&", "="));
        }
        if (isTypePost()) {
            identity = identity.concat(proteu.getRequestPost().getInFormat("&", "="));
        }
        if (isTypeCookie()) {
            identity = identity.concat(proteu.getRequestCookie().getInFormat("&", "="));
        }
        if (isTypeSession()) {
            identity = identity.concat(proteu.getSession().getInFormat("&", "="));
        }
        if (isTypeSessionId()) {
            String session = "";
            if (proteu.isEnterprise()) {
                session = proteu.getRequestCookie().getString("JSESSIONID");
            } else {
                session = proteu.getRequestCookie().getString("proteu_session");
            }
            identity = identity.concat(session);
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] b = identity.getBytes();
        md5.update(b, 0, b.length);
        return getCacheFileNamePrefix().concat(new BigInteger(1, md5.digest()).toString(16));
    }
