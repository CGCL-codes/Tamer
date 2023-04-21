        public void run() {
            BufferedWriter bufWriter = null;
            try {
                PipedWriter writer = new PipedWriter(reader);
                bufWriter = new BufferedWriter(new ColorFilterWriter(writer));
                start();
                character.export(handler, bufWriter);
            } catch (IOException ex) {
                Logging.errorPrint("Unable to construct piped writer", ex);
            } finally {
                try {
                    if (bufWriter != null) {
                        bufWriter.close();
                    }
                } catch (IOException ex) {
                    Logging.errorPrint("Unable to close PipedWriter", ex);
                }
            }
            interupted = Thread.interrupted();
        }
