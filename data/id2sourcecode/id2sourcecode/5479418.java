    public void createInvertedIndex() {
        try {
            Runtime r = Runtime.getRuntime();
            logger.debug("creating inverted index");
            final String LexiconFilename = indexPathPrefix + ApplicationSetup.LEXICONSUFFIX;
            final int numberOfDocuments = index.getCollectionStatistics().getNumberOfDocuments();
            long assumedNumberOfPointers = Long.parseLong(index.getIndexProperty("num.Pointers", "0"));
            long numberOfTokens = 0;
            long numberOfPointers = 0;
            LexiconInputStream lexiconStream = getLexInputStream(LexiconFilename);
            numberOfUniqueTerms = lexiconStream.numberOfEntries();
            final int fieldsCount = FieldScore.FIELDS_COUNT;
            DataOutputStream dos = new DataOutputStream(Files.writeFileStream(LexiconFilename.concat(".tmp2")));
            if (processTerms > numberOfUniqueTerms) processTerms = (int) numberOfUniqueTerms;
            long startProcessingLexicon = 0;
            long startTraversingDirectFile = 0;
            long startWritingInvertedFile = 0;
            long numberOfPointersThisIteration = 0;
            int i = 0;
            int iterationCounter = 0;
            String iteration_message_suffix = null;
            if (numberOfPointersPerIteration > 0 || processTerms == 0) {
                if (assumedNumberOfPointers > 0) {
                    iteration_message_suffix = " of " + ((assumedNumberOfPointers % numberOfPointersPerIteration == 0) ? (assumedNumberOfPointers / numberOfPointersPerIteration) : 1 + (assumedNumberOfPointers / numberOfPointersPerIteration)) + " iterations";
                } else {
                    iteration_message_suffix = "";
                }
            } else {
                iteration_message_suffix = " of " + ((numberOfUniqueTerms % processTerms == 0) ? (numberOfUniqueTerms / processTerms) : 1 + (numberOfUniqueTerms / processTerms)) + " iterations";
            }
            if (numberOfPointersPerIteration == 0) {
                logger.warn("Using old-fashioned number of terms strategy. Please consider setting invertedfile.processpointers for forward compatible use");
            }
            while (i < numberOfUniqueTerms) {
                iterationCounter++;
                TIntIntHashMap codesHashMap = null;
                TIntArrayList[][] tmpStorage = null;
                IntLongTuple results = null;
                logger.info("Iteration " + iterationCounter + iteration_message_suffix);
                startProcessingLexicon = System.currentTimeMillis();
                if (numberOfPointersPerIteration > 0) {
                    if (logger.isDebugEnabled()) logger.debug("Scanning lexicon for " + numberOfPointersPerIteration + " pointers");
                    codesHashMap = new TIntIntHashMap();
                    ArrayList<TIntArrayList[]> tmpStorageStorage = new ArrayList<TIntArrayList[]>();
                    results = scanLexiconForPointers(numberOfPointersPerIteration, lexiconStream, codesHashMap, tmpStorageStorage);
                    tmpStorage = (TIntArrayList[][]) tmpStorageStorage.toArray(new TIntArrayList[0][0]);
                } else {
                    if (logger.isDebugEnabled()) logger.debug("Scanning lexicon for " + processTerms + " terms");
                    tmpStorage = new TIntArrayList[processTerms][];
                    codesHashMap = new TIntIntHashMap(processTerms);
                    results = scanLexiconForTerms(processTerms, lexiconStream, codesHashMap, tmpStorage);
                }
                processTerms = results.Terms;
                numberOfPointersThisIteration = results.Pointers;
                numberOfPointers += results.Pointers;
                i += processTerms;
                if (logger.isDebugEnabled()) logger.debug("time to process part of lexicon: " + ((System.currentTimeMillis() - startProcessingLexicon) / 1000D));
                displayMemoryUsage(r);
                startTraversingDirectFile = System.currentTimeMillis();
                traverseDirectFile(codesHashMap, tmpStorage);
                if (logger.isDebugEnabled()) logger.debug("time to traverse direct file: " + ((System.currentTimeMillis() - startTraversingDirectFile) / 1000D));
                displayMemoryUsage(r);
                startWritingInvertedFile = System.currentTimeMillis();
                numberOfTokens += writeInvertedFilePart(dos, tmpStorage, processTerms);
                if (logger.isDebugEnabled()) logger.debug("time to write inverted file: " + ((System.currentTimeMillis() - startWritingInvertedFile) / 1000D));
                displayMemoryUsage(r);
                if (logger.isDebugEnabled()) {
                    logger.debug("time to perform one iteration: " + ((System.currentTimeMillis() - startProcessingLexicon) / 1000D));
                    logger.debug("number of pointers processed: " + numberOfPointersThisIteration);
                }
                tmpStorage = null;
                codesHashMap.clear();
                codesHashMap = null;
            }
            file.close();
            this.numberOfDocuments = numberOfDocuments;
            this.numberOfTokens = numberOfTokens;
            this.numberOfUniqueTerms = numberOfUniqueTerms;
            this.numberOfPointers = numberOfPointers;
            lexiconStream.close();
            dos.close();
            LexiconInputStream lis = getLexInputStream(LexiconFilename);
            LexiconOutputStream los = getLexOutputStream(LexiconFilename.concat(".tmp3"));
            DataInputStream dis = new DataInputStream(Files.openFileStream(LexiconFilename.concat(".tmp2")));
            while (lis.readNextEntryBytes() != -1) {
                los.writeNextEntry(lis.getTermCharacters(), lis.getTermId(), lis.getNt(), dis.readInt(), dis.readLong(), dis.readByte());
            }
            lis.close();
            los.close();
            dis.close();
            if (!Files.delete(LexiconFilename)) logger.error("delete file .lex failed!");
            if (!Files.delete(LexiconFilename.concat(".tmp2"))) logger.error("delete file .lex.tmp2 failed!");
            if (!Files.rename(LexiconFilename.concat(".tmp3"), LexiconFilename)) logger.error("rename file .lex.tmp3 to .lex failed!");
            index.addIndexStructure("inverted", "uk.ac.gla.terrier.structures.InvertedIndex", "uk.ac.gla.terrier.structures.Lexicon,java.lang.String,java.lang.String", "lexicon,path,prefix");
            index.addIndexStructureInputStream("inverted", "uk.ac.gla.terrier.structures.InvertedIndexInputStream", "java.lang.String,java.lang.String,uk.ac.gla.terrier.structures.LexiconInputStream", "path,prefix,lexicon-inputstream");
            index.setIndexProperty("num.inverted.fields.bits", "" + FieldScore.FIELDS_COUNT);
            index.setIndexProperty("num.Terms", "" + numberOfUniqueTerms);
            index.setIndexProperty("num.Tokens", "" + numberOfTokens);
            index.setIndexProperty("num.Pointers", "" + numberOfPointers);
            System.gc();
        } catch (IOException ioe) {
            logger.error("IOException occured during creating the inverted file. Stack trace follows.", ioe);
        }
    }
