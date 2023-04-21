    private void createAndSegment(CommandLine commandLine, SrxDocument document, Reader reader, Writer writer, boolean profile) throws IOException {
        if (profile) {
            System.out.println("Segmenting... ");
        }
        long start = System.currentTimeMillis();
        TextIterator textIterator = createTextIterator(commandLine, document, reader, profile);
        performSegment(commandLine, textIterator, writer, profile);
        if (profile) {
            System.out.println(System.currentTimeMillis() - start + " ms.");
        }
    }
