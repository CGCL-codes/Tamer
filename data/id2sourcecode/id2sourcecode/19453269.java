        public CasSNPMatrixGenerator(CasIdLookup referenceIdLookup, CasIdLookup readIdLookup, CasGappedReferenceMap gappedReferenceMap, DataStore<NucleotideEncodedGlyphs> nucleotideDataStore, TrimDataStore trimDataStore, long referenceIdToGenerateSNPsFor, List<Integer> gappedSNPCoordinates, PrintWriter writer) {
            super(referenceIdLookup, readIdLookup, gappedReferenceMap, nucleotideDataStore, trimDataStore);
            this.referenceIdToGenerateSNPsFor = referenceIdToGenerateSNPsFor;
            this.gappedSNPCoordinates.addAll(gappedSNPCoordinates);
            this.writer = writer;
        }
