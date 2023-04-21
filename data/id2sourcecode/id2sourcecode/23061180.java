    public byte[] engineSign() throws SignatureException {
        if (privateKey == null) throw new SignatureException("not initialized for signing");
        try {
            BigInteger g = privateKey.getParams().getG();
            BigInteger p = privateKey.getParams().getP();
            BigInteger q = privateKey.getParams().getQ();
            BigInteger x = privateKey.getX();
            BigInteger k = new BigInteger(159, appRandom != null ? appRandom : random);
            BigInteger r = g.modPow(k, p);
            r = r.mod(q);
            byte bytes[] = digest.digest();
            BigInteger sha = new BigInteger(1, bytes);
            BigInteger s = sha.add(x.multiply(r));
            s = s.multiply(k.modInverse(q)).mod(q);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ArrayList seq = new ArrayList(2);
            seq.add(0, new DERValue(DER.INTEGER, r));
            seq.add(1, new DERValue(DER.INTEGER, s));
            DERWriter.write(bout, new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, seq));
            return bout.toByteArray();
        } catch (IOException ioe) {
            SignatureException se = new SignatureException();
            se.initCause(ioe);
            throw se;
        } catch (ArithmeticException ae) {
            SignatureException se = new SignatureException();
            se.initCause(ae);
            throw se;
        }
    }
