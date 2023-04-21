    protected byte[] engineSign() throws SignatureException {
        BigInteger one = BigInteger.ONE;
        BigInteger k, kInv, x, r, s, shaM;
        int qBitLength = 160;
        byte[] out, rBytes, sBytes;
        byte[] shaMBytes = md_.digest();
        shaM = new BigInteger(1, shaMBytes);
        do {
            k = new BigInteger(qBitLength, secureRandom_);
        } while (k.compareTo(one) <= 0 || k.compareTo(q_) >= 0);
        kInv = k.modInverse(q_);
        x = dsaPrivateKey_.getX();
        r = (g_.modPow(k, p_)).mod(q_);
        s = (kInv.multiply((shaM.add(x.multiply(r))))).mod(q_);
        rBytes = r.toByteArray();
        sBytes = s.toByteArray();
        try {
            ASN1DSASignature asn1signature = new ASN1DSASignature(r, s);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEREncoder encoder = new DEREncoder(baos);
            asn1signature.encode(encoder);
            byte[] os = baos.toByteArray();
            baos.close();
            return os;
        } catch (ConstraintException ce) {
            ce.printStackTrace();
            throw new SignatureException("shouldn't happen");
        } catch (ASN1Exception ae) {
            ae.printStackTrace();
            throw new SignatureException("shouldn't happen");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new SignatureException("shouldn't happen");
        }
    }
