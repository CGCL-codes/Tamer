    protected void run(BufferedReader input, PrintStream output) {
        final LanguagePair languagePair = new LanguagePair(sourceLanguageArgument.getValue(), targetLanguageArgument.getValue());
        final CorpusWriters writerType = writerArgument.getValue();
        final CorpusWriter writer = writerType.newInstance(output, languagePair);
        if (writerType == CorpusWriters.MULTI) init((MultiFileCorpusWriter) writer, outputDirArgument.getValue(), languagePair);
        final CorpusReaders readerType = readerArgument.getValue();
        CorpusReader reader = readerType.newInstance(input, languagePair);
        if (readerType == CorpusReaders.MULTI) init((MultiFileCorpusReader) reader, inputDirArgument.getValue(), languagePair);
        reader = new SampledCorpusReader(reader, sampleFrequencyArgument.getValue(), sampleWindowArgument.getValue());
        MultiSentence sentence;
        while ((sentence = reader.next()) != null) writer.write(sentence);
        writer.complete();
    }
