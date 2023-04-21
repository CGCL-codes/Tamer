    @Override
    protected void open(String aPassphrase) throws EndOfFileException, IOException, UnsupportedFileVersionException {
        LOG.enterMethod("PwsFileV3.init");
        setPassphrase(new StringBuilder(aPassphrase));
        if (storage != null) {
            inStream = new ByteArrayInputStream(storage.load());
            lastStorageChange = storage.getModifiedDate();
        }
        PwsFileHeaderV3 theHeaderV3 = new PwsFileHeaderV3(this);
        setHeaderV3(theHeaderV3);
        int iter = theHeaderV3.getIter();
        LOG.debug1("Using iterations: [" + iter + "]");
        final SHA256Pws shaHasher = new SHA256Pws();
        stretchedPassword = Util.stretchPassphrase(aPassphrase.getBytes(), theHeaderV3.getSalt(), iter);
        if (!Util.bytesAreEqual(theHeaderV3.getPassword(), shaHasher.digest(stretchedPassword))) {
            CharBuffer buf = CharBuffer.wrap(aPassphrase);
            stretchedPassword = Util.stretchPassphrase(Charset.defaultCharset().encode(buf).array(), theHeaderV3.getSalt(), iter);
            if (Util.bytesAreEqual(theHeaderV3.getPassword(), shaHasher.digest(stretchedPassword))) {
                LOG.warn("Succeeded workaround for asymmetric password encoding bug");
            } else {
                throw new IOException("Invalid password");
            }
        }
        try {
            byte[] rka = TwofishPws.processECB(stretchedPassword, false, theHeaderV3.getB1());
            byte[] rkb = TwofishPws.processECB(stretchedPassword, false, theHeaderV3.getB2());
            decryptedRecordKey = Util.mergeBytes(rka, rkb);
            byte[] hka = TwofishPws.processECB(stretchedPassword, false, theHeaderV3.getB3());
            byte[] hkb = TwofishPws.processECB(stretchedPassword, false, theHeaderV3.getB4());
            decryptedHmacKey = Util.mergeBytes(hka, hkb);
            hasher = new HmacPws(decryptedHmacKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading encrypted fields");
        }
        twofishCbc = new TwofishPws(decryptedRecordKey, false, theHeaderV3.getIV());
        readExtraHeader(this);
        LOG.leaveMethod("PwsFileV3.init");
    }
