    public static void writeAceFile(Contig<AcePlacedRead> contig, SliceMap sliceMap, DataStore<Phd> phdDataStore, OutputStream out, boolean calculateBestSegments) throws IOException, DataStoreException {
        final NucleotideEncodedGlyphs consensus = contig.getConsensus();
        StringBuilder bestSegmentBuilder = new StringBuilder();
        if (calculateBestSegments) {
            System.out.println("calculating best segments...");
            AceBestSegmentMap bestSegments = new OnTheFlyAceBestSegmentMap(sliceMap, consensus);
            int numberOfBestSegments = 0;
            for (AceBestSegment bestSegment : bestSegments) {
                numberOfBestSegments++;
                final Range gappedConsensusRange = bestSegment.getGappedConsensusRange().convertRange(CoordinateSystem.RESIDUE_BASED);
                bestSegmentBuilder.append(String.format("BS %d %d %s%n", gappedConsensusRange.getLocalStart(), gappedConsensusRange.getLocalEnd(), bestSegment.getReadName()));
            }
            writeString(String.format(CONTIG_HEADER, contig.getId(), consensus.getLength(), contig.getNumberOfReads(), numberOfBestSegments, "U"), out);
        } else {
            writeString(String.format(CONTIG_HEADER, contig.getId(), consensus.getLength(), contig.getNumberOfReads(), 0, "U"), out);
        }
        writeString(String.format("%s%n%n", AceFileUtil.convertToAcePaddedBasecalls(consensus)), out);
        writeUngappedConsensusQualities(consensus, sliceMap, out);
        writeString(String.format("%n"), out);
        List<AssembledFrom> assembledFroms = getSortedAssembledFromsFor(contig);
        for (AssembledFrom assembledFrom : assembledFroms) {
            String id = assembledFrom.getId();
            long fullLength = phdDataStore.get(id).getBasecalls().getLength();
            writeAssembledFromRecords(contig.getPlacedReadById(id), fullLength, out);
        }
        out.flush();
        if (calculateBestSegments) {
            writeString(bestSegmentBuilder.toString(), out);
            writeString(String.format("%n"), out);
        }
        out.flush();
        for (AssembledFrom assembledFrom : assembledFroms) {
            String id = assembledFrom.getId();
            AcePlacedRead read = contig.getPlacedReadById(id);
            writePlacedRead(read, phdDataStore.get(id), out);
        }
        out.flush();
    }
