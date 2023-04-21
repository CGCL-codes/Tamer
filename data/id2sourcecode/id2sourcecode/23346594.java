    protected void computerVerifyDataSSLv3(byte[] sender, byte[] buf) {
        MessageDigest md5;
        MessageDigest sha;
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            fatalAlert(AlertProtocol.INTERNAL_ERROR, "Could not initialize the Digest Algorithms.", e);
            return;
        }
        try {
            byte[] hanshake_messages = io_stream.getMessages();
            md5.update(hanshake_messages);
            md5.update(sender);
            md5.update(session.master_secret);
            byte[] b = md5.digest(SSLv3Constants.MD5pad1);
            md5.update(session.master_secret);
            md5.update(SSLv3Constants.MD5pad2);
            System.arraycopy(md5.digest(b), 0, buf, 0, 16);
            sha.update(hanshake_messages);
            sha.update(sender);
            sha.update(session.master_secret);
            b = sha.digest(SSLv3Constants.SHApad1);
            sha.update(session.master_secret);
            sha.update(SSLv3Constants.SHApad2);
            System.arraycopy(sha.digest(b), 0, buf, 16, 20);
        } catch (Exception e) {
            fatalAlert(AlertProtocol.INTERNAL_ERROR, "INTERNAL ERROR", e);
        }
    }
