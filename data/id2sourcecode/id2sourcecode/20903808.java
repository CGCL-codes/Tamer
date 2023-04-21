    protected void initMechanism() throws SaslException {
        final MD5 md = new MD5();
        byte[] b;
        b = authorizationID.getBytes();
        md.update(b, 0, b.length);
        b = serverName.getBytes();
        md.update(b, 0, b.length);
        b = protocol.getBytes();
        md.update(b, 0, b.length);
        if (channelBinding.length > 0) md.update(channelBinding, 0, channelBinding.length);
        uid = Util.toBase64(md.digest());
        if (ClientStore.instance().isAlive(uid)) {
            final SecurityContext ctx = ClientStore.instance().restoreSession(uid);
            srp = SRP.instance(ctx.getMdName());
            sid = ctx.getSID();
            K = ctx.getK();
            cIV = ctx.getClientIV();
            sIV = ctx.getServerIV();
            replayDetection = ctx.hasReplayDetection();
            inCounter = ctx.getInCounter();
            outCounter = ctx.getOutCounter();
            inMac = ctx.getInMac();
            outMac = ctx.getOutMac();
            inCipher = ctx.getInCipher();
            outCipher = ctx.getOutCipher();
        } else {
            sid = new byte[0];
            ttl = 0;
            K = null;
            cIV = null;
            sIV = null;
            cn = null;
            sn = null;
        }
    }
