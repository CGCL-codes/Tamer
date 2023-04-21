    public TungstenProperties getStatistics() {
        TungstenProperties sessionProps = new TungstenProperties();
        long readXactCount = slaveHitCount + slaveMissCount;
        double slaveHitPercentage = 0;
        if (readXactCount > 0) {
            slaveHitPercentage = (((double) slaveHitCount / (double) readXactCount)) * 100;
        }
        double readWriteRatio = 0;
        if (writeXactCount > 0) {
            readWriteRatio = ((double) readXactCount / ((double) (writeXactCount + readXactCount))) * 100;
        }
        sessionProps.setString("sessionId", sessionId);
        sessionProps.setString("highWater", highWater.toString());
        sessionProps.setLong("timeCreated", timeCreated);
        sessionProps.setLong("lastTimeUsed", lastTimeUsed);
        sessionProps.setLong("sessionAccessCount", sessionAccessCount);
        sessionProps.setLong("writeXactCount", writeXactCount);
        sessionProps.setLong("readXactCount", readXactCount);
        sessionProps.setLong("slaveHitCount", slaveHitCount);
        sessionProps.setLong("slaveMissCount", slaveMissCount);
        sessionProps.setDouble("slaveHitPercentage", slaveHitPercentage);
        sessionProps.setDouble("readWriteRatio", readWriteRatio);
        sessionProps.setLong("switchToMaster", switchToMaster);
        sessionProps.setLong("switchToSlave", switchToSlave);
        return sessionProps;
    }
