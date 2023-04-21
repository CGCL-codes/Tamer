    public void filter(String input, String output) {
        File outputFile = new File(output);
        SamReadPairReader reader = new SamReadPairReader(input);
        final SAMFileWriter writer = new SAMFileWriterFactory().makeSAMOrBAMWriter(reader.getHeader(), false, outputFile);
        for (ReadPair pair : reader) {
            SAMRecord read1 = pair.getRead1();
            SAMRecord read2 = pair.getRead2();
            boolean isPairIncluded = !shouldStripIndels || !hasIndels(read1, read2);
            if (isPairIncluded) {
                isPairIncluded = !isMaxInsertLenExceeded(read1, read2);
            }
            if (isPairIncluded) {
                writer.addAlignment(read1);
                writer.addAlignment(read2);
            }
        }
        writer.close();
        reader.close();
    }
