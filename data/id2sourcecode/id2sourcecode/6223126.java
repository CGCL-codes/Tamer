        public void write(byte b[]) throws IOException {
            if (debug_write) {
                z.reportln("Entering RXTXPort:SerialOutputStream:write(" + b.length + ") ");
            }
            if (speed == 0) return;
            if (monThreadisInterrupted == true) {
                return;
            }
            if (fd == 0) throw new IOException();
            IOLocked++;
            waitForTheNativeCodeSilly();
            try {
                writeArray(b, 0, b.length, monThreadisInterrupted);
                if (debug_write) z.reportln("Leaving RXTXPort:SerialOutputStream:write(" + b.length + ")");
            } catch (IOException e) {
                IOLocked--;
                throw e;
            }
            IOLocked--;
        }
