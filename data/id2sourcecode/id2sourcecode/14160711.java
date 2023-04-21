        public byte getByte() {
            synchronized (md) {
                boff++;
                if (boff % buf.length == 0) {
                    BigInteger Z = BigInteger.ONE.shiftLeft(buf.length * 9).subtract(BigInteger.ONE);
                    jk = jk.modPow(BigInteger.valueOf(3141526L), Z).shiftRight(9);
                    boff = 0;
                    md.update(buf);
                    md.update(jk.toByteArray());
                    buf = md.digest();
                }
                return buf[boff];
            }
        }
