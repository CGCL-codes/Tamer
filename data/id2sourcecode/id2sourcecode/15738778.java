    private void update(String passphrase, PwsFileV3 file) {
        LOG.enterMethod("PwsFileHeaderV3.update");
        byte[] stretchedPassword = PwsFileV3.stretchPassphrase(passphrase.getBytes(), salt, Util.getIntFromByteArray(iter, 0));
        password = SHA256Pws.digest(stretchedPassword);
        byte[] b1pt = new byte[16];
        Util.newRandBytes(b1pt);
        byte[] b2pt = new byte[16];
        Util.newRandBytes(b2pt);
        b1 = TwofishPws.processECB(stretchedPassword, true, b1pt);
        b2 = TwofishPws.processECB(stretchedPassword, true, b2pt);
        file.decryptedRecordKey = Util.mergeBytes(b1pt, b2pt);
        byte[] b3pt = new byte[16];
        Util.newRandBytes(b3pt);
        byte[] b4pt = new byte[16];
        Util.newRandBytes(b4pt);
        b3 = TwofishPws.processECB(stretchedPassword, true, b3pt);
        b4 = TwofishPws.processECB(stretchedPassword, true, b4pt);
        file.decryptedHmacKey = Util.mergeBytes(b3pt, b4pt);
        file.hasher = new HmacPws(file.decryptedHmacKey);
        LOG.leaveMethod("PwsFileHeaderV3.update");
    }
