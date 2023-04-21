    protected void open(String passphrase) throws EndOfFileException, IOException, UnsupportedFileVersionException {
        LOG.enterMethod("PwsFileV3.init");
        Passphrase = passphrase;
        if (storage != null) {
            InStream = new ByteArrayInputStream(storage.load());
        }
        headerV3 = new PwsFileHeaderV3(this);
        int iter = Util.getIntFromByteArray(headerV3.getIter(), 0);
        LOG.debug1("Using iterations: [" + iter + "]");
        stretchedPassword = stretchPassphrase(passphrase.getBytes(), headerV3.getSalt(), iter);
        if (!Util.bytesAreEqual(headerV3.getPassword(), SHA256Pws.digest(stretchedPassword))) {
            throw new IOException("Invalid password");
        }
        try {
            byte[] rka = TwofishPws.processECB(stretchedPassword, false, headerV3.getB1());
            byte[] rkb = TwofishPws.processECB(stretchedPassword, false, headerV3.getB2());
            decryptedRecordKey = Util.mergeBytes(rka, rkb);
            byte[] hka = TwofishPws.processECB(stretchedPassword, false, headerV3.getB3());
            byte[] hkb = TwofishPws.processECB(stretchedPassword, false, headerV3.getB4());
            decryptedHmacKey = Util.mergeBytes(hka, hkb);
            hasher = new HmacPws(decryptedHmacKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading encrypted fields");
        }
        twofishCbc = new TwofishPws(decryptedRecordKey, false, headerV3.getIV());
        readExtraHeader(this);
        LOG.leaveMethod("PwsFileV3.init");
    }
