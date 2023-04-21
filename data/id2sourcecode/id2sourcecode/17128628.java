    @Override
    public String align(String ontology1, String ontology2, String inputAlignment, List<ArgumentType> parameters) throws KADMOSFault {
        if (ontology1 == null || ontology1.isEmpty()) throw new KADMOSFault("Parameter \"ontology 1\" must be provided!");
        if (ontology2 == null || ontology2.isEmpty()) throw new KADMOSFault("Parameter \"ontology 2\" must be provided!");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        if (!isRemoteAccessibility(ontology1)) throw new KADMOSFault("Ontology given by parameter \"ontology 1\" is not accessible");
        try {
            ont1 = manager.loadOntologyFromOntologyDocument(IRI.create(ontology1));
        } catch (OWLOntologyCreationException e) {
            logger.debug(e.getMessage());
            throw new KADMOSFault("Cannot create \"ontology 1\" from parameter \"ontology 1\"");
        }
        if (!isRemoteAccessibility(ontology2)) throw new KADMOSFault("Ontology given by parameter \"ontology 2\" is not accessible");
        try {
            ont2 = manager.loadOntologyFromOntologyDocument(IRI.create(ontology2));
        } catch (OWLOntologyCreationException e) {
            logger.debug(e.getMessage());
            throw new KADMOSFault("Cannot create \"ontology 2\" from parameter \"ontology 2\"");
        }
        logger.info("URI of Ontology 1: " + ontology1);
        logger.info("URI of Ontology 2: " + ontology2);
        Alignment inputAlignmentTmp = null;
        if (inputAlignment != null) {
            if (inputAlignment.isEmpty()) throw new KADMOSFault("Parameter \"inputAlignment\" is empty!");
            URL url;
            try {
                url = new URL(inputAlignment);
            } catch (MalformedURLException e) {
                logger.debug(e.getMessage());
                throw new KADMOSFault("Provided URL of input alignment is malformed!");
            }
            Reader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                logger.debug(e.getMessage());
                throw new KADMOSFault("Cannot read in alignment provided!");
            }
            AlignmentParser parser = INRIAFormatParser.getInstance();
            try {
                inputAlignmentTmp = parser.parse(reader);
            } catch (AlignmentParserException e) {
                logger.debug(e.getMessage());
                throw new KADMOSFault("Cannot parse alignment from provided URL!");
            } catch (IllegalArgumentException e) {
                logger.debug(e.getMessage());
                throw new KADMOSFault("Cannot parse alignment from provided URL!");
            } catch (IOException e) {
                logger.debug(e.getMessage());
                throw new KADMOSFault("Cannot parse alignment from provided URL!");
            }
        }
        Properties properties = buildProperties(parameters);
        AlgorithmType algoType = AlgorithmType.MAPPSO;
        String algoTypeProp = properties.getProperty("algorithm");
        logger.info(algoTypeProp);
        if ((algoTypeProp != null) && AlgorithmType.contains(algoTypeProp)) {
            algoType = AlgorithmType.valueOf(algoTypeProp);
        }
        logger.info("Type of Algorithm used for alignment processing: " + algoType);
        logger.info("Start processing of alignment algorithm...");
        Alignment alignment = null;
        try {
            switch(algoType) {
                case MAPPSO:
                    logger.debug("Processing MapPSO starts...");
                    MapPSOAlignmentAlgorithm mapPSOAlgo = new MapPSOAlignmentAlgorithm();
                    mapPSOAlgo.setParameters(properties);
                    if (inputAlignmentTmp != null) {
                        alignment = mapPSOAlgo.align(ont1, ont2, inputAlignmentTmp);
                    } else {
                        alignment = mapPSOAlgo.align(ont1, ont2);
                    }
                    logger.debug("Processing MapPSO finished.");
                    break;
                case MAPEVO:
                    logger.debug("Processing MapEVO starts...");
                    MapEVOAlignmentAlgorithm mapEVOAlgo = new MapEVOAlignmentAlgorithm();
                    mapEVOAlgo.setParameters(properties);
                    alignment = mapEVOAlgo.align(ont1, ont2);
                    logger.debug("Processing MapEVO finished.");
                    break;
            }
        } catch (AlignmentAlgorithmException e) {
            logger.debug(e.getMessage());
            throw new KADMOSFault("An exception has been occured during alignment algorithm processing.", e);
        } catch (IncompatibleAlignmentsException e) {
            logger.debug(e.getMessage());
            throw new KADMOSFault("The input alignment provided is incompatible with the alignment algorithm");
        }
        logger.info("Finished processing of alignment algorithm.");
        logger.info("Preparing alignment to return...");
        StringWriter renderingBuffer = null;
        try {
            renderingBuffer = new StringWriter();
            PrintWriter writer = new PrintWriter(renderingBuffer);
            AlignmentRenderer renderer = INRIAFormatRenderer.getInstance(writer);
            renderer.render(alignment);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            throw new KADMOSFault("An exception occured while preparing alignment for printing.");
        }
        String url = makeRemoteAccessibleFileFromAlignment(renderingBuffer.toString(), fileName, fileType);
        return url;
    }
