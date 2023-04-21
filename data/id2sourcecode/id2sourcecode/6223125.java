        public void write(int b) throws IOException {
            if (debug_write) z.reportln("RXTXPort:SerialOutputStream:write(int)");
            if (speed == 0) return;
            if (monThreadisInterrupted == true) {
                return;
            }
            IOLocked++;
            waitForTheNativeCodeSilly();
            if (fd == 0) {
                IOLocked--;
                throw new IOException();
            }
            try {
                writeByte(b, monThreadisInterrupted);
                if (debug_write) z.reportln("Leaving RXTXPort:SerialOutputStream:write( int )");
            } catch (IOException e) {
                IOLocked--;
                throw e;
            }
            IOLocked--;
        }
