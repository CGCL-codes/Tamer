        public void write(byte b[], int off, int len) throws IOException {
            if (speed == 0) return;
            if (off + len > b.length) {
                throw new IndexOutOfBoundsException("Invalid offset/length passed to read");
            }
            byte send[] = new byte[len];
            System.arraycopy(b, off, send, 0, len);
            if (debug_write) {
                z.reportln("Entering RXTXPort:SerialOutputStream:write(" + send.length + " " + off + " " + len + " " + ") ");
            }
            if (fd == 0) throw new IOException();
            if (monThreadisInterrupted == true) {
                return;
            }
            IOLocked++;
            waitForTheNativeCodeSilly();
            try {
                writeArray(send, 0, len, monThreadisInterrupted);
                if (debug_write) z.reportln("Leaving RXTXPort:SerialOutputStream:write(" + send.length + " " + off + " " + len + " " + ") ");
            } catch (IOException e) {
                IOLocked--;
                throw e;
            }
            IOLocked--;
        }
