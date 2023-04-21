    private void loadOperationsBeans(SessionState sessionState, URL url) throws IOException, JAXBException {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("  + " + url);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            OperationsBeans beansDefs = OperationsBeansHelper.load(reader);
            logger.debug("Loaded operations beans descriptor from " + url);
            int i = 0;
            for (Bean beanDesc : beansDefs.getBean()) {
                try {
                    loadBeanOperations(sessionState, beanDesc);
                } catch (BeanOperationLoadException e) {
                    logger.error("Could not load bean (" + i + ") from '" + url + "': " + e.getMessage());
                }
                i++;
            }
        } catch (IOException e) {
            logger.error("Could not read from " + url + ", contents will not be available", e);
            throw (IOException) e.fillInStackTrace();
        } catch (JAXBException e) {
            logger.error("Could not parse " + url + ", contents will not be available", e);
            throw (JAXBException) e.fillInStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
