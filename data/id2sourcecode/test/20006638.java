        @Override
        public boolean visitReadData(SFFReadData readData) {
            Range forwardClearRange = nexteraTransposonTrimmer.trim(readData.getBasecalls(), forwardTransposonDataStore);
            Range reverseClearRange = nexteraTransposonTrimmer.trim(readData.getBasecalls(), reverseTransposonDataStore);
            final Range clearRange;
            if (reverseClearRange.isSubRangeOf(forwardClearRange)) {
                clearRange = Range.buildRange(CoordinateSystem.RESIDUE_BASED, forwardClearRange.getLocalStart(), reverseClearRange.getLocalEnd());
            } else {
                clearRange = forwardClearRange.intersection(reverseClearRange);
            }
            if (forwardClearRange.getStart() != 0) {
                numberOfTrimmedReads++;
                try {
                    DefaultSFFReadHeader.Builder builder = new DefaultSFFReadHeader.Builder(currentReadHeader);
                    builder.qualityClip(clearRange);
                    SffWriter.writeReadHeader(builder.build(), tempOut);
                    SffWriter.writeReadData(readData, tempOut);
                } catch (IOException e) {
                    throw new IllegalStateException("error writing read data to temp", e);
                }
            } else {
                System.out.println("skipping " + currentReadHeader.getName());
            }
            return true;
        }
