    private void segment(CommandLine commandLine) throws IOException {
        Reader reader = null;
        Writer writer = null;
        try {
            boolean profile = commandLine.hasOption('p');
            boolean twice = commandLine.hasOption('2');
            boolean preload = commandLine.hasOption('r');
            if (twice && !profile) {
                throw new RuntimeException("Can only repeat segmentation twice in profile mode.");
            }
            reader = createTextReader(commandLine, profile, twice, preload);
            writer = createTextWriter(commandLine);
            if (preload) {
                preloadText(reader, profile);
            }
            SrxDocument document = createSrxDocument(commandLine, profile);
            createAndSegment(commandLine, document, reader, writer, profile);
            if (twice) {
                reader = createTextReader(commandLine, profile, twice, preload);
                createAndSegment(commandLine, document, reader, writer, profile);
            }
        } finally {
            cleanupReader(reader);
            cleanupWriter(writer);
        }
    }
