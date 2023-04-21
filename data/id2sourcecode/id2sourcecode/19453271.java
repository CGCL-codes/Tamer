    public static void main(String[] args) throws IOException, SFFDecoderException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("cas", "input cas file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("out", "output file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("id", "contig id").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("v", "file of variations log").isRequired(true).build());
        try {
            CommandLine commandLine = CommandLineUtils.parseCommandLine(options, args);
            File casFile = new File(commandLine.getOptionValue("cas"));
            String contigId = commandLine.getOptionValue("id");
            boolean isGapped = false;
            String outputFilePath = commandLine.getOptionValue("out");
            File variationsFile = new File(commandLine.getOptionValue("v"));
            DefaultVariationLogFile varaintMap = new DefaultVariationLogFile(variationsFile);
            final FastQQualityCodec illuminaQualityCodec = FastQQualityCodec.ILLUMINA;
            MultiCasDataStoreFactory casDataStoreFactory = new MultiCasDataStoreFactory(new H2SffCasDataStoreFactory(DirectoryFileServer.createTemporaryDirectoryFileServer(new File("/usr/local/scratch/dkatzel/")), EmptyDataStoreFilter.INSTANCE), new H2FastQCasDataStoreFactory(illuminaQualityCodec), new FastaCasDataStoreFactory(100));
            AbstractDefaultCasFileLookup readIdLookup = new DefaultReadCasFileLookup();
            AbstractDefaultCasFileLookup referenceIdLookup = new DefaultReferenceCasFileLookup();
            AbstractCasFileNucleotideDataStore nucleotideDataStore = new ReadCasFileNucleotideDataStore(casDataStoreFactory);
            AbstractCasFileNucleotideDataStore referenceNucleotideDataStore = new ReferenceCasFileNucleotideDataStore(casDataStoreFactory);
            DefaultCasFileQualityDataStore qualityDataStore = new DefaultCasFileQualityDataStore(casDataStoreFactory);
            System.out.println("parsing dataStore info...");
            CasParser.parseCas(casFile, MultipleWrapper.createMultipleWrapper(CasFileVisitor.class, readIdLookup, referenceIdLookup, nucleotideDataStore, referenceNucleotideDataStore, qualityDataStore));
            DefaultCasGappedReferenceMap gappedReferenceMap = new DefaultCasGappedReferenceMap(referenceNucleotideDataStore, referenceIdLookup);
            System.out.println("parsing gapped references...");
            CasParser.parseCas(casFile, gappedReferenceMap);
            long casReferenceId = referenceIdLookup.getCasIdFor(contigId);
            NucleotideEncodedGlyphs consensus = gappedReferenceMap.getGappedReferenceFor(casReferenceId);
            List<Integer> gappedSNPCoordinates = new ArrayList<Integer>();
            Map<Integer, Integer> gappedCoordinateToPositionMap = new HashMap<Integer, Integer>();
            for (long coordinate : varaintMap.getVariationsFor(contigId).keySet()) {
                final int gappedOffset;
                if (isGapped) {
                    gappedOffset = (int) coordinate - 1;
                } else {
                    int ungappedOffset = (int) coordinate - 1;
                    gappedOffset = consensus.convertUngappedValidRangeIndexToGappedValidRangeIndex(ungappedOffset);
                }
                gappedSNPCoordinates.add(gappedOffset);
                gappedCoordinateToPositionMap.put(gappedOffset, (int) coordinate);
            }
            SffTrimDataStore sffTrimDatastore = new SffTrimDataStore();
            for (File readFile : readIdLookup.getFiles()) {
                String extension = FilenameUtils.getExtension(readFile.getName());
                if ("sff".equals(extension)) {
                    SffParser.parseSFF(readFile, sffTrimDatastore);
                }
            }
            PrintWriter writer = new PrintWriter(new FileOutputStream(outputFilePath), true);
            writer.printf("#id\t%s%n", contigId);
            writer.printf("#loc\t%s%n", new StringUtilities.JoinedStringBuilder(varaintMap.getVariationsFor(contigId).keySet()).glue('\t').build());
            CasSNPMatrixGenerator snpGenerator = new CasSNPMatrixGenerator(referenceIdLookup, readIdLookup, gappedReferenceMap, nucleotideDataStore, sffTrimDatastore, casReferenceId, gappedSNPCoordinates, writer);
            System.out.println("parsing SNPs...");
            CasParser.parseCas(casFile, snpGenerator);
            writer.close();
        } catch (ParseException e) {
            printHelp(options);
            System.exit(1);
        }
    }
