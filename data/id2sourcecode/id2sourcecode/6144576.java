    public static void reverse(String output) {
        BitFile newFile = new BitFile(output, "rw");
        Lexicon lexicon = new Lexicon();
        OldBitFile file = new OldBitFile(ApplicationSetup.INVERTED_FILENAME, "rw");
        try {
            newFile.writeReset();
            for (int currentTerm = 0; currentTerm < lexicon.getNumberOfLexiconEntries(); currentTerm++) {
                lexicon.seekEntry(currentTerm);
                byte startBitOffset = lexicon.getStartBitOffset();
                long startOffset = lexicon.getStartOffset();
                byte endBitOffset = lexicon.getEndBitOffset();
                long endOffset = lexicon.getEndOffset();
                final int fieldCount = FieldScore.FIELDS_COUNT;
                final boolean loadTagInformation = FieldScore.USE_FIELD_INFORMATION;
                int df = lexicon.getNt();
                file.readReset(startOffset, startBitOffset, endOffset, endBitOffset);
                if (loadTagInformation) {
                    for (int i = 0; i < df; i++) {
                        newFile.writeGamma(file.readGamma());
                        newFile.writeUnary(file.readUnary());
                        newFile.writeBinary(file.readBinary(fieldCount), fieldCount);
                    }
                } else {
                    for (int i = 0; i < df; i++) {
                        newFile.writeGamma(file.readGamma());
                        newFile.writeUnary(file.readUnary());
                    }
                }
            }
            newFile.close();
            file.close();
            lexicon.close();
        } catch (IOException e) {
            System.err.println("Error writting the compressed file ");
            e.printStackTrace();
        }
    }
