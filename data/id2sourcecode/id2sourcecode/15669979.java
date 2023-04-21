    private void parse(DerValue val) throws CRLException, IOException {
        if (readOnly) throw new CRLException("cannot over-write existing CRL"); else if (val.getData() == null || DerValue.tag_Sequence != val.tag) throw new CRLException("Invalid DER-encoded CRL data"); else {
            this.signedCRL = val.toByteArray();
            DerInputStream data = val.data;
            DerValue seq0Certlist = data.getDerValue();
            DerValue seq1Algid = data.getDerValue();
            DerValue seq2Signa = data.getDerValue();
            if (0 != data.available()) throw new CRLException("signed overrun, bytes = " + data.available()); else if (DerValue.tag_Sequence != seq0Certlist.tag) throw new CRLException("signed CRL fields invalid"); else {
                this.sigAlgId = AlgorithmId.parse(seq1Algid);
                this.signature = seq2Signa.getBitString();
                if (0 != seq1Algid.data.available()) throw new CRLException("AlgorithmId field overrun"); else if (0 != seq2Signa.data.available()) throw new CRLException("Signature field overrun"); else {
                    this.tbsCertList = seq0Certlist.toByteArray();
                    data = seq0Certlist.data;
                    DerValue tmp;
                    byte nextByte;
                    this.version = 0;
                    nextByte = (byte) data.peekByte();
                    if (DerValue.tag_Integer == nextByte) {
                        this.version = data.getInteger();
                        if (1 != this.version) throw new CRLException("Invalid version");
                    }
                    tmp = data.getDerValue();
                    AlgorithmId tmpId = AlgorithmId.parse(tmp);
                    if (!tmpId.equals(this.sigAlgId)) throw new CRLException("Signature algorithm mismatch"); else {
                        this.infoSigAlgId = tmpId;
                        this.issuer = new X500Name(data);
                        if (this.issuer.isEmpty()) {
                            throw new CRLException("Empty issuer DN not allowed in X509CRLs");
                        } else {
                            nextByte = (byte) data.peekByte();
                            switch(nextByte) {
                                case DerValue.tag_UtcTime:
                                    thisUpdate = data.getUTCTime();
                                    break;
                                case DerValue.tag_GeneralizedTime:
                                    thisUpdate = data.getGeneralizedTime();
                                    break;
                                default:
                                    throw new CRLException("Invalid encoding for thisUpdate (tag=" + nextByte + ")");
                            }
                            if (data.available() == 0) return; else {
                                nextByte = (byte) data.peekByte();
                                switch(nextByte) {
                                    case DerValue.tag_UtcTime:
                                        nextUpdate = data.getUTCTime();
                                        break;
                                    case DerValue.tag_GeneralizedTime:
                                        nextUpdate = data.getGeneralizedTime();
                                        break;
                                    default:
                                        break;
                                }
                                if (data.available() == 0) return; else {
                                    nextByte = (byte) data.peekByte();
                                    if ((DerValue.tag_SequenceOf == nextByte) && ((nextByte & 0x0c0) != 0x080)) {
                                        DerValue[] badCerts = data.getSequence(4);
                                        X500Name crlIssuer = this.issuer;
                                        X500Name badCertIssuer = crlIssuer;
                                        Map<X509IssuerSerial, X509CRLEntry> revokedCerts = this.revokedCerts;
                                        for (int i = 0; i < badCerts.length; i++) {
                                            X509CRLEntry entry = new X509CRLEntry(badCerts[i]);
                                            badCertIssuer = Enter(revokedCerts, entry, crlIssuer, badCertIssuer);
                                        }
                                    }
                                    if (data.available() == 0) return; else {
                                        tmp = data.getDerValue();
                                        if (tmp.isConstructed() && tmp.isContextSpecific((byte) 0)) {
                                            extensions = new CRLExtensions(tmp.data);
                                        }
                                        this.readOnly = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
