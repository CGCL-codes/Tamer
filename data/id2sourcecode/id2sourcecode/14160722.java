        public boolean verify(byte[] data, int off, int len, byte[] signature) throws CraiException {
            try {
                byte[] arg = new byte[20];
                System.arraycopy(signature, 0, arg, 0, 20);
                final BigInteger r = new BigInteger(1, arg);
                System.arraycopy(signature, 20, arg, 0, 20);
                final BigInteger s = new BigInteger(1, arg);
                IMessageDigest md = new Sha160();
                md.update(data, off, len);
                byte[] sig = md.digest();
                return checkRS(r, s, sig);
            } catch (Exception e) {
                throw new CraiException("error verifying DSA signature: " + e);
            }
        }
