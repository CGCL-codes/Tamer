        public void write(byte val) {
            low.write(val);
            high.write(highTempReg.read());
        }
