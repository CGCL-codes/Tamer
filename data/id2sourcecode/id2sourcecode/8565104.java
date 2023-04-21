        public void run() {
            IOException e = null;
            try {
                while (true) {
                    int readVal = in.read();
                    if (readVal < 0) {
                        break;
                    }
                    write(readVal);
                }
            } catch (IOException t) {
                e = t;
            } catch (Throwable t) {
                e = new IOException("exception during read");
                e.initCause(t);
            } finally {
                signalEOF(e);
            }
        }
