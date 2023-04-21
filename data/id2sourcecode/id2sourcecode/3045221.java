    public synchronized TungstenProperties getSessionStatistics() {
        int currentSessionCount = 0;
        long slaveHitCount = 0;
        long slaveMissCount = 0;
        long writeXactCount = 0;
        long readXactCount = 0;
        long sessionAccessCount = 0;
        long switchToSlaveCount = 0;
        long switchToMasterCount = 0;
        for (TSRSession session : values()) {
            currentSessionCount++;
            sessionAccessCount += session.getSessionAccessCount();
            slaveHitCount += session.getSlaveHitCount();
            slaveMissCount += session.getSlaveMissCount();
            writeXactCount += session.getWriteXactCount();
            switchToMasterCount += session.getSwitchToMaster();
            switchToSlaveCount += session.getSwitchToSlave();
        }
        readXactCount = slaveHitCount + slaveMissCount;
        TungstenProperties sessionProps = new TungstenProperties();
        if (accumulatedReadXactCount + readXactCount > 0) {
            accumulatedSlaveHitPercentage = ((((double) accumulatedSlaveHitCount + slaveHitCount) / (double) (accumulatedReadXactCount + readXactCount))) * 100;
        }
        double slaveHitPercentage = 0;
        if (readXactCount > 0) {
            slaveHitPercentage = (((double) slaveHitCount / (double) readXactCount)) * 100;
        }
        double readWriteRatio = 0;
        if (writeXactCount > 0) {
            readWriteRatio = ((double) readXactCount / ((double) (writeXactCount + readXactCount))) * 100;
        }
        if (accumulatedWriteXactCount + writeXactCount > 0) {
            accumulatedReadWriteRatio = (((double) accumulatedReadXactCount + readXactCount) / ((double) (accumulatedWriteXactCount + writeXactCount + accumulatedReadXactCount + readXactCount))) * 100;
        }
        sessionProps.setLong("accumulatedSessionAccessCount", accumulatedSessionAccessCount + sessionAccessCount);
        sessionProps.setLong("accumulatedSessionCount", accumulatedSessionCount + currentSessionCount);
        sessionProps.setLong("accumulatedSlaveHitCount", accumulatedSlaveHitCount + slaveHitCount);
        sessionProps.setLong("accumulatedSlaveMissCount", accumulatedSlaveMissCount + slaveMissCount);
        sessionProps.setDouble("accumulatedSlaveHitPercentage", accumulatedSlaveHitPercentage);
        sessionProps.setDouble("accumulatedReadWriteRatio", accumulatedReadWriteRatio);
        sessionProps.setLong("accumulatedSwitchToMasterCount", accumulatedSwitchToMasterCount + switchToMasterCount);
        sessionProps.setLong("accumulatedSwitchToSlaveCount", accumulatedSwitchToMasterCount + switchToSlaveCount);
        sessionProps.setInt("currentSessionCount", currentSessionCount);
        sessionProps.setLong("sessionAccessCount", sessionAccessCount);
        sessionProps.setLong("writeXactCount", writeXactCount);
        sessionProps.setLong("readXactCount", readXactCount);
        sessionProps.setLong("slaveHitCount", slaveHitCount);
        sessionProps.setLong("slaveMissCount", slaveMissCount);
        sessionProps.setDouble("slaveHitPercentage", slaveHitPercentage);
        sessionProps.setDouble("readWriteRatio", readWriteRatio);
        sessionProps.setLong("switchToMasterCount", switchToMasterCount);
        sessionProps.setLong("switchToSlaveCount", switchToSlaveCount);
        return sessionProps;
    }
