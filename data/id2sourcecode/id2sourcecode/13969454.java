        public SangerTrimFilesCreator addTrimmedRead(String readId, NucleotideSequence fullLengthBasecalls, Range qualityClearRange, Range vectorClearRange, Range clearRange) {
            unTrimmedFasta.print(new DefaultNucleotideSequenceFastaRecord(readId, fullLengthBasecalls));
            trimmedFasta.print(new DefaultNucleotideSequenceFastaRecord(readId, Nucleotides.asString(fullLengthBasecalls.asList(clearRange))));
            writeTrimPoints(trimPoints, readId, clearRange);
            if (qualityClearRange != null) {
                writeTrimPoints(qualityTrimPoints, readId, qualityClearRange);
            }
            if (vectorClearRange != null) {
                writeTrimPoints(vectorTrimPoints, readId, vectorClearRange);
            }
            return this;
        }
