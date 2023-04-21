    protected boolean engineVerify(byte[] signature) {
        try {
            ASN1DSASignature asn1Signature = new ASN1DSASignature();
            ByteArrayInputStream bais = new ByteArrayInputStream(signature);
            DERDecoder decoder = new DERDecoder(bais);
            asn1Signature.decode(decoder);
            decoder.close();
            byte[] rBytes, sBytes, shaMBytes;
            BigInteger r, s, w, u1, u2, shaM, v, y;
            y = dsaPublicKey_.getY();
            r = asn1Signature.getR();
            s = asn1Signature.getS();
            if (r.compareTo(q_) >= 0 || s.compareTo(q_) >= 0) {
                return false;
            }
            shaMBytes = md_.digest();
            shaM = new BigInteger(1, shaMBytes);
            w = s.modInverse(q_);
            u1 = (shaM.multiply(w)).mod(q_);
            u2 = (r.multiply(w)).mod(q_);
            v = (((g_.modPow(u1, p_)).multiply(y.modPow(u2, p_))).mod(p_)).mod(q_);
            if (r.compareTo(v) == 0) return true; else {
                return false;
            }
        } catch (ASN1Exception ae) {
            System.out.println("shouldn't happen");
            ae.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("shouldn't happen");
            ioe.printStackTrace();
        }
        return false;
    }
