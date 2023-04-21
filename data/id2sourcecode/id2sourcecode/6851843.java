    private void retrieveBlock(final String hashcode) throws DSSException {
        logger.debug("retrieveBlock(" + hashcode + ")");
        String errMsg;
        String[] location = indexConnection.getLocation(hashcode);
        if (indexConnection == null) {
            logger.debug("indexConnetion is null");
        }
        if (location == null) {
            logger.debug("location is null");
        }
        logger.debug("Apparently hashcode " + hashcode + " is at the following " + location.length + " locations:");
        for (int i = 0; i < location.length; i++) {
            logger.debug(location[i]);
        }
        boolean retrieved = false;
        if (location.length > 0) {
            for (int j = 0; j < location.length && !retrieved; j++) {
                String remoteStoreID = location[j];
                logger.debug("Checking " + remoteStoreID);
                if (remoteStoreID.equals(storeID)) {
                    logger.debug("This service is location " + storeID + " so ignoring");
                } else {
                    logger.debug("Appears to be remote service, so will attempt to get proxy...");
                    Object dssService = null;
                    try {
                        logger.debug("about to getSearcher()...");
                        ServiceSearcher dssServiceSearcher = thisServiceContext.getSearcher();
                        logger.debug("about to getService()...");
                        Object[] services = dssServiceSearcher.getService(DSSService.class, new String[] { "DSS", remoteStoreID });
                        logger.debug("Found " + services.length + " services");
                        dssService = services[0];
                        if (dssService != null) {
                            logger.debug("services[0] was not null");
                            DSSService targetService = (DSSService) dssService;
                            logger.debug("going to retrieve, and write...");
                            localStore.write(targetService.read(hashcode));
                            indexConnection.setLocation(hashcode, storeID, 100000);
                            retrieved = true;
                        }
                    } catch (IOException e) {
                        errMsg = "Unexpected IOException when attempting to retrieve block locally: " + e.getMessage();
                        logger.error(errMsg);
                        throw new DSSException(errMsg);
                    } catch (ServerServentException e) {
                        errMsg = "Unexpected ServerServentException when attempting to retrieve block locally: " + e.getMessage();
                        logger.error(errMsg);
                        throw new DSSException(errMsg);
                    }
                }
            }
        } else {
            throw new DSSException("The hashcode " + hashcode + " cannot be located on the DSS");
        }
    }
