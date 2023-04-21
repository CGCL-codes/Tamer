    private void parse(DerValue val) throws CertificateException, IOException {
        if (readOnly) throw new CertificateParsingException("cannot over-write existing certificate");
        if (val.data == null) throw new CertificateParsingException("invalid DER-encoded certificate data");
        signedCert = val.toByteArray();
        DerValue[] seq = new DerValue[3];
        seq[0] = val.data.getDerValue();
        seq[1] = val.data.getDerValue();
        seq[2] = val.data.getDerValue();
        if (val.data.available() != 0) {
            throw new CertificateParsingException("signed overrun, bytes = " + val.data.available());
        }
        if (seq[0].tag != DerValue.tag_Sequence) {
            throw new CertificateParsingException("signed fields invalid");
        }
        algId = AlgorithmId.parse(seq[1]);
        signature = seq[2].getBitString();
        if (seq[1].data.available() != 0) {
            throw new CertificateParsingException("algid field overrun");
        }
        if (seq[2].data.available() != 0) throw new CertificateParsingException("signed fields overrun");
        info = new X509CertInfo(seq[0]);
        AlgorithmId infoSigAlg = (AlgorithmId) info.get(CertificateAlgorithmId.NAME + DOT + CertificateAlgorithmId.ALGORITHM);
        if (!algId.equals(infoSigAlg)) throw new CertificateException("Signature algorithm mismatch");
        readOnly = true;
    }
