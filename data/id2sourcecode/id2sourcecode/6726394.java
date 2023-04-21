        public void run() {
            PipedWriter writer = null;
            try {
                writer = new PipedWriter(reader);
                start();
                character.export(new ExportHandler(templateFile), new BufferedWriter(writer, 1));
            } catch (IOException ex) {
                Logging.errorPrint("Unable to construct piped writer", ex);
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException ex) {
                    Logging.errorPrint("Unable to close PipedWriter", ex);
                }
            }
        }
