    protected void run(BufferedReader input, PrintStream output) {
        final DocumentWriter<Phrase> writer = new SimpleDocumentWriter(output, langArgument.getValue());
        final DocumentReaders readerType = readerArgument.getValue();
        DocumentReader<Phrase> reader = readerType.newInstance(input, langArgument.getValue());
        if (readerType == DocumentReaders.MULTI) init((MultiFileDocumentReader) reader, inputDirArgument.getValue());
        if (!sampleSentencesArgument.isDefault()) {
            final BufferedReader sampleSentencesReader = BufferedReaderManager.open(sampleSentencesArgument.getValue());
            reader = new PreSampledDocumentReader<Phrase>(reader, sampleSentencesReader);
        }
        reader = new SampledDocumentReader<Phrase>(reader, sampleFrequencyArgument.getValue(), sampleWindowArgument.getValue());
        Sentence<Phrase> sentence;
        while ((sentence = reader.next()) != null) writer.write(sentence);
        writer.complete();
    }
