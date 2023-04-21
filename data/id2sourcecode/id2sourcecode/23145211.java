    public void sign(PrivateKey key, String algorithm, String provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        try {
            if (readOnly) throw new CertificateEncodingException("cannot over-write existing certificate");
            Signature sigEngine = null;
            if ((provider == null) || (provider.length() == 0)) sigEngine = Signature.getInstance(algorithm); else sigEngine = Signature.getInstance(algorithm, provider);
            sigEngine.initSign(key);
            algId = AlgorithmId.get(sigEngine.getAlgorithm());
            DerOutputStream out = new DerOutputStream();
            DerOutputStream tmp = new DerOutputStream();
            info.encode(tmp);
            byte[] rawCert = tmp.toByteArray();
            algId.encode(tmp);
            sigEngine.update(rawCert, 0, rawCert.length);
            signature = sigEngine.sign();
            tmp.putBitString(signature);
            out.write(DerValue.tag_Sequence, tmp);
            signedCert = out.toByteArray();
            readOnly = true;
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }
