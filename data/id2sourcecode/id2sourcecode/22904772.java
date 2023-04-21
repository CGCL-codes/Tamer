    private void parse(DerValue val) throws CRLException, IOException {
        if (readOnly) throw new CRLException("cannot over-write existing CRL");
        signedCRL = val.toByteArray();
        DerValue seq[] = new DerValue[3];
        seq[0] = val.data.getDerValue();
        seq[1] = val.data.getDerValue();
        seq[2] = val.data.getDerValue();
        if (val.data.available() != 0) throw new CRLException("signed overrun, bytes = " + val.data.available());
        if (seq[0].tag != DerValue.tag_Sequence) throw new CRLException("signed CRL fields invalid");
        sigAlgId = AlgorithmId.parse(seq[1]);
        signature = seq[2].getBitString();
        if (seq[1].data.available() != 0) throw new CRLException("AlgorithmId field overrun");
        if (seq[2].data.available() != 0) throw new CRLException("Signature field overrun");
        tbsCertList = seq[0].toByteArray();
        DerInputStream derStrm = seq[0].data;
        DerValue tmp;
        byte nextByte;
        version = 0;
        nextByte = (byte) derStrm.peekByte();
        if (nextByte == DerValue.tag_Integer) {
            version = derStrm.getInteger();
            if (version != 1) throw new CRLException("Invalid version");
        }
        tmp = derStrm.getDerValue();
        AlgorithmId tmpId = AlgorithmId.parse(tmp);
        if (!tmpId.equals(sigAlgId)) throw new CRLException("Signature algorithm mismatch");
        infoSigAlgId = tmpId;
        issuer = new X500Name(derStrm);
        String issuerString = issuer.toString();
        if ((issuerString == null || issuerString.length() == 0) && version == 0) throw new CRLException("Null Issuer DN allowed only in v2 CRL");
        nextByte = (byte) derStrm.peekByte();
        if (nextByte == DerValue.tag_UtcTime) {
            thisUpdate = derStrm.getUTCTime();
        } else if (nextByte == DerValue.tag_GeneralizedTime) {
            thisUpdate = derStrm.getGeneralizedTime();
        } else {
            throw new CRLException("Invalid encoding for thisUpdate" + " (tag=" + nextByte + ")");
        }
        if (derStrm.available() == 0) return;
        nextByte = (byte) derStrm.peekByte();
        if (nextByte == DerValue.tag_UtcTime) {
            nextUpdate = derStrm.getUTCTime();
        } else if (nextByte == DerValue.tag_GeneralizedTime) {
            nextUpdate = derStrm.getGeneralizedTime();
        }
        if (derStrm.available() == 0) return;
        nextByte = (byte) derStrm.peekByte();
        if ((nextByte == DerValue.tag_SequenceOf) && (!((nextByte & 0x0c0) == 0x080))) {
            DerValue[] badCerts = derStrm.getSequence(4);
            for (int i = 0; i < badCerts.length; i++) {
                X509CRLEntryImpl entry = new X509CRLEntryImpl(badCerts[i]);
                revokedCerts.put(entry.getSerialNumber(), (X509CRLEntry) entry);
            }
        }
        if (derStrm.available() == 0) return;
        tmp = derStrm.getDerValue();
        if (tmp.isConstructed() && tmp.isContextSpecific((byte) 0)) {
            extensions = new CRLExtensions(tmp.data);
        }
        readOnly = true;
    }
