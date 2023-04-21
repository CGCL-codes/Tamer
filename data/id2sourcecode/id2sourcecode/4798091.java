    @Test
    public void retrieveURL() throws IOException {
        boolean execute = true;
        try {
            URL url = new URL("http://www.redlab.be/test/test.css");
            url.openConnection().connect();
        } catch (SocketTimeoutException e) {
            LOG.info("Skipping retrieve from URL test as we cannot open the url itself. Maybe no internet connection. Marking test as Success");
            execute = false;
        } catch (IOException e) {
            LOG.info("Skipping retrieve from URL test as we cannot open the url itself. Maybe no internet connection. Marking test as Success");
            execute = false;
        }
        if (execute) {
            final FileOutputStream out = new FileOutputStream(actual);
            retriever.processFromHref("http://www.redlab.be/test/test.css", new ReadingProcessor() {

                public void process(final int inbit) {
                    try {
                        out.write((char) inbit);
                    } catch (IOException e) {
                        throw new RuntimeWorkerException(e);
                    }
                }
            });
            out.close();
        }
    }
