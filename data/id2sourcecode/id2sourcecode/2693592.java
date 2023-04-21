    public Raindrop(String aURI) throws StartupException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Element root;
        try {
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.parse(aURI);
            root = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            logger.log(Level.FINER, "Unable to parse the configuration URI: " + aURI, e);
            throw new StartupException("Unable to parse the configuration URI: " + aURI, e);
        } catch (SAXException e) {
            logger.log(Level.FINER, "A problem has occurred in processing the configuration URI: " + aURI, e);
            throw new StartupException("A problem has occurred in processing the configuration URI: " + aURI, e);
        } catch (IOException e) {
            logger.log(Level.FINER, "A problem has occurred in accessing the configuration URI: " + aURI, e);
            throw new StartupException("A problem has occurred in accessing the configuration URI: " + aURI, e);
        }
        ResponseTimeMonitorThread rtMonitorThread = new ResponseTimeMonitorThread();
        Thread rtMonitorThreadT = new Thread(rtMonitorThread);
        rtMonitorThreadT.setDaemon(true);
        rtMonitorThreadT.start();
        long threadPoolUpdate = 1000;
        manager = new StageManager(threadPoolUpdate);
        try {
            createNIO();
        } catch (IOException e) {
            logger.log(Level.FINER, "A problem has occurred in setting up the NIO subsystem.", e);
            throw new StartupException("A problem has occurred in setting up the NIO subsystem.", e);
        }
        NodeList admissionControllerNodes = root.getElementsByTagName("admissioncontrollers");
        logger.info("Number of admission controllers found: " + admissionControllerNodes.getLength());
        admissionControllers = createAdmissionControllers(admissionControllerNodes);
        NodeList stages = root.getElementsByTagName("stage");
        logger.info("Number of stages found: " + stages.getLength());
        createStages(stages, rtMonitorThread.getChannel());
        logger.info("stagegetlength=" + stages.getLength());
        initialiseStages(stages);
    }
