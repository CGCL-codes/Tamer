    public void write(OutputStream out, ContigCheckerStruct<T> struct) throws IOException, DataStoreException {
        final NucleotideEncodedGlyphs consensus = struct.getContig().getConsensus();
        for (PlacedRead read : struct.getContig().getPlacedReads()) {
            EncodedGlyphs<PhredQuality> fullQualities = struct.getQualityDataStore().get(read.getId());
            if (fullQualities != null) {
                final Map<Integer, NucleotideGlyph> snps = read.getSnps();
                StringBuilder snpBuilder = new StringBuilder();
                for (Entry<Integer, NucleotideGlyph> snp : snps.entrySet()) {
                    Integer gappedIndex = snp.getKey();
                    final NucleotideGlyph snpBasecall = snp.getValue();
                    if (!snpBasecall.isGap()) {
                        PhredQuality qualityValue = qualityValueStrategy.getQualityFor(read, fullQualities, gappedIndex);
                        int consensusOffset = (int) (read.getStart() + gappedIndex);
                        int fullRangeIndex = read.getEncodedGlyphs().convertGappedValidRangeIndexToUngappedValidRangeIndex(gappedIndex);
                        snpBuilder.append(String.format(SNP_FORMAT, fullRangeIndex, snpBasecall, qualityValue.getNumber(), consensusOffset, consensus.get(consensusOffset)));
                    }
                }
                if (snpBuilder.length() > 0) {
                    out.write(String.format(READ_BEGIN_TAG, read.getId()).getBytes());
                    out.write(snpBuilder.toString().getBytes());
                    out.write(READ_END_TAG.getBytes());
                }
            }
        }
    }
