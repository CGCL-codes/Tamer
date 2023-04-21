    protected int engineSign(byte[] outbuffer, int offset, int length) throws SignatureException {
        BigInteger one = BigInteger.ONE;
        BigInteger k, kInv, x, r, s, shaM;
        int qBitLength = 160;
        byte[] out, rBytes, sBytes;
        byte[] shaMBytes = md_.digest();
        shaM = new BigInteger(1, shaMBytes);
        k = new BigInteger(qBitLength - 1, secureRandom_);
        kInv = k.modInverse(q_);
        x = dsaPrivateKey_.getX();
        r = (g_.modPow(k, p_)).mod(q_);
        s = kInv.multiply((shaM.add(x.multiply(r)))).mod(q_);
        rBytes = r.toByteArray();
        sBytes = s.toByteArray();
        try {
            ASN1DSASignature asn1signature = new ASN1DSASignature(r, s);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEREncoder encoder = new DEREncoder(baos);
            asn1signature.encode(encoder);
            byte[] os = baos.toByteArray();
            baos.close();
            int osLength = os.length;
            if (length >= osLength) {
                System.arraycopy(os, 0, outbuffer, offset, osLength);
                return osLength;
            } else {
                return 0;
            }
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
