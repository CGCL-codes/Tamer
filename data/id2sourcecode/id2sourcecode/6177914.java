        public void write(byte val) {
            reg.write((tempHighReg.read() << 8) + val);
        }
