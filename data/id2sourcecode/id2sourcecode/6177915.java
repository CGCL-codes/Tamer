        public byte read() {
            tempHighReg.write((byte) (reg.read16() >> 8));
            return (byte) reg.read16();
        }
