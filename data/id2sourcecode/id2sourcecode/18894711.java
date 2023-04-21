    public CertHashExtension(byte[] cert, String alg) throws ASN1Exception, GeneralSecurityException {
        if (cert == null) {
            throw new NullPointerException("cert");
        }
        AlgorithmIdentifier aid;
        MessageDigest dig;
        byte[] buf;
        aid = new AlgorithmIdentifier(alg);
        dig = MessageDigest.getInstance(alg);
        buf = dig.digest(cert);
        syntax_ = new ASN1Sequence(2);
        hashAlgorithm_ = aid;
        certificateHash_ = new ASN1OctetString(buf);
        syntax_.add(hashAlgorithm_);
        syntax_.add(certificateHash_);
        setOID(new ASN1ObjectIdentifier(EXTENSION_OID));
        setCritical(false);
        setValue(syntax_);
    }
