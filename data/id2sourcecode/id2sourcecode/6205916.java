    private BufferedReader getReader(String id) throws CGateException {
        try {
            PipedWriter piped_writer = new PipedWriter();
            BufferedWriter out = new BufferedWriter(piped_writer);
            response_writers.put(id, out);
            PipedReader piped_reader = new PipedReader(piped_writer);
            return new BufferedReader(piped_reader);
        } catch (IOException e) {
            throw new CGateException(e);
        }
    }
