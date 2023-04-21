        public OurRandom() {
            synchronized (md) {
                if (buf == null) {
                    md.update(("" + System.currentTimeMillis()).getBytes());
                    buf = md.digest();
                    jk = new BigInteger(1, buf);
                }
            }
        }
