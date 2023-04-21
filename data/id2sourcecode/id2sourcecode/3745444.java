    public byte[] engineSign() throws SignatureException {
        if (digest == null) throw new SignatureException();
        if (privateKey == null) throw new SignatureException();
        try {
            BigInteger g = privateKey.getParams().getG();
            BigInteger p = privateKey.getParams().getP();
            BigInteger q = privateKey.getParams().getQ();
            BigInteger x = privateKey.getX();
            BigInteger k = new BigInteger(159, (Random) appRandom);
            BigInteger r = g.modPow(k, p);
            r = r.mod(q);
            byte bytes[] = digest.digest();
            BigInteger sha = new BigInteger(1, bytes);
            BigInteger s = sha.add(x.multiply(r));
            s = s.multiply(k.modInverse(q)).mod(q);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ArrayList seq = new ArrayList(2);
            seq.add(new DERValue(DER.INTEGER, r));
            seq.add(new DERValue(DER.INTEGER, s));
            DERWriter.write(bout, new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, seq));
            return bout.toByteArray();
        } catch (IOException ioe) {
            throw new SignatureException();
        } catch (ArithmeticException ae) {
            throw new SignatureException();
        }
    }
