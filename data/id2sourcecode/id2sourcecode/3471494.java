    public void run() throws IOException {
        if (writeSpringConfiguration) {
            SpringConfigurationXMLBeanConverter converter = new SpringConfigurationXMLBeanConverter();
            XmlObject xml;
            if (configuration == null) {
                Resource confFileR = new FileSystemResource(confFile);
                configuration = new ConfParserConfiguration(confFileR);
                xml = converter.convert(configuration);
            } else {
                xml = converter.convert(configuration);
            }
            String springFilename = confFile.getCanonicalPath().replace(".conf", ".xml");
            File springFile = new File(springFilename);
            if (springFile.exists()) {
                logger.warn("Cannot write Spring configuration, because " + springFilename + " already exists.");
            } else {
                Files.createFile(springFile, xml.toString());
            }
        }
        if (performCrossValidation) {
            AbstractReasonerComponent rs = context.getBean(AbstractReasonerComponent.class);
            PosNegLP lp = context.getBean(PosNegLP.class);
            AbstractCELA la = context.getBean(AbstractCELA.class);
            new CrossValidation(la, lp, rs, nrOfFolds, false);
        } else {
            for (Entry<String, LearningAlgorithm> entry : context.getBeansOfType(LearningAlgorithm.class).entrySet()) {
                algorithm = entry.getValue();
                logger.info("Running algorithm instance \"" + entry.getKey() + "\"(" + algorithm.getClass().getSimpleName() + ")");
                algorithm.start();
            }
        }
    }
