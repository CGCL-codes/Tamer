        private void doBind() throws IOException, InterruptedException {
            PipedWriter writer = null;
            PipedReader reader = null;
            WriterStreamBinder binder = null;
            try {
                File xlsFile = new File(fileName);
                Parse parse = new ExcelSheetCollector().collectTable(xlsFile);
                writer = new PipedWriter();
                reader = new PipedReader(writer);
                binder = new WriterStreamBinder(reader, ostream);
                binder.start();
                parse.print(new PrintWriter(writer));
            } finally {
                if (writer != null) {
                    writer.close();
                }
                if (binder != null) {
                    binder.join();
                    if (!error) {
                        error = binder.isError();
                    }
                }
                ostream.close();
                if (reader != null) {
                    reader.close();
                }
            }
        }
