    private <O extends Observation, D extends Opdf<O>> void opdfCheck(String opdfString, OpdfReader<D> reader, OpdfWriter<D> writer) throws IOException {
        try {
            PipedWriter pw = new PipedWriter();
            PipedReader pr = new PipedReader(pw);
            StreamTokenizer st = new StreamTokenizer(new StringReader(opdfString));
            D opdf = reader.read(st);
            writer.write(pw, opdf);
            reader.read(new StreamTokenizer(pr));
        } catch (FileFormatException e) {
            fail(e.toString());
        }
    }
