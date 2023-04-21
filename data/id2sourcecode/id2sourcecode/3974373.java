    private void update(String aPassphrase, PwsFileV3 file) {
        LOG.enterMethod("PwsFileHeaderV3.update");
        final byte[] stretchedPassword = Util.stretchPassphrase(aPassphrase.getBytes(), salt, iter);
        final SHA256Pws hasher = new SHA256Pws();
        password = hasher.digest(stretchedPassword);
        final byte[] b1pt = new byte[16];
        Util.newRandBytes(b1pt);
        final byte[] b2pt = new byte[16];
        Util.newRandBytes(b2pt);
        b1 = TwofishPws.processECB(stretchedPassword, true, b1pt);
        b2 = TwofishPws.processECB(stretchedPassword, true, b2pt);
        file.decryptedRecordKey = Util.mergeBytes(b1pt, b2pt);
        final byte[] b3pt = new byte[16];
        Util.newRandBytes(b3pt);
        final byte[] b4pt = new byte[16];
        Util.newRandBytes(b4pt);
        b3 = TwofishPws.processECB(stretchedPassword, true, b3pt);
        b4 = TwofishPws.processECB(stretchedPassword, true, b4pt);
        file.decryptedHmacKey = Util.mergeBytes(b3pt, b4pt);
        file.hasher = new HmacPws(file.decryptedHmacKey);
        LOG.leaveMethod("PwsFileHeaderV3.update");
    }
