    public void updateStats() {
        if (logger.isTraceEnabled()) {
            logger.trace("updating sip manager " + container.getApplicationName() + " statistics");
        }
        long now = System.currentTimeMillis();
        int elapsedNumberOfSasCreationCounter = sipApplicationSessionCounter - lastUpdatedSasCreationCounter;
        if (elapsedNumberOfSasCreationCounter > 0) {
            double elapsedSasCreationUpdatedTimeInSeconds = (now - lastSipApplicationSessionUpdatedTime) / 1000;
            double elapsedAverageSasCreationPerSecond = elapsedNumberOfSasCreationCounter / elapsedSasCreationUpdatedTimeInSeconds;
            lastAverageSasCreationPerSecond = (lastAverageSasCreationPerSecond + elapsedAverageSasCreationPerSecond) / 2;
        }
        lastUpdatedSasCreationCounter = sipApplicationSessionCounter;
        lastSipApplicationSessionUpdatedTime = now;
        if (logger.isTraceEnabled()) {
            logger.trace("elapsedNumberOfSasCreationCounter " + elapsedNumberOfSasCreationCounter);
            logger.trace("lastUpdatedSasCreationCounter " + lastUpdatedSasCreationCounter);
            logger.trace("lastSipApplicationSessionUpdatedTime " + lastSipApplicationSessionUpdatedTime);
        }
        int elapsedNumberOfSsCreationCounter = sipSessionCounter - lastUpdatedSsCreationCounter;
        if (elapsedNumberOfSsCreationCounter > 0) {
            double elapsedSsCreationUpdatedTimeInSeconds = (now - lastSipSessionUpdatedTime) / 1000;
            double elapsedAverageSsCreationPerSecond = elapsedNumberOfSsCreationCounter / elapsedSsCreationUpdatedTimeInSeconds;
            lastAverageSsCreationPerSecond = (lastAverageSsCreationPerSecond + elapsedAverageSsCreationPerSecond) / 2;
        }
        lastUpdatedSsCreationCounter = sipSessionCounter;
        lastSipSessionUpdatedTime = now;
        if (logger.isTraceEnabled()) {
            logger.trace("elapsedNumberOfSsCreationCounter " + elapsedNumberOfSsCreationCounter);
            logger.trace("lastUpdatedSsCreationCounter " + lastUpdatedSsCreationCounter);
            logger.trace("lastSipSessionUpdatedTime " + lastSipSessionUpdatedTime);
        }
    }
