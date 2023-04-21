    public void sign(PrivateKey keyPrivate, X500Signer signer) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (readOnly) throw new CertificateEncodingException("cannot over-write existing certificate"); else {
            Signature sigEngine = signer.getSignature();
            sigEngine.initSign(keyPrivate);
            AlgorithmId algId = signer.getAlgorithmId();
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
        }
    }
