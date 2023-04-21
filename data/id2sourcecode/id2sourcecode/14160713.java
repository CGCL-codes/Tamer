        public byte[] sign(byte[] b, int off, int len) throws CraiException {
            try {
                IMessageDigest md = new Sha160();
                md.update(b, off, len);
                byte[] sig = md.digest();
                BigInteger[] rs = computeRS(sig);
                byte[] rb = rs[0].toByteArray();
                byte[] sb = rs[1].toByteArray();
                sig = new byte[40];
                System.arraycopy(rb, rb.length - 20, sig, 0, 20);
                System.arraycopy(sb, sb.length - 20, sig, 20, 20);
                return sig;
            } catch (Exception e) {
                throw new CraiException("error performing DSA signature: " + e);
            }
        }
