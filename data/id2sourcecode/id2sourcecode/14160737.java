        public void finish(byte[] out, int off) throws CraiException {
            try {
                byte[] buf = mMac.digest();
                System.arraycopy(buf, 0, out, off, buf.length);
            } catch (Exception x) {
                throw new CraiException(x.toString());
            }
        }
