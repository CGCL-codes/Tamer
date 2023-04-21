    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        MapInfo mapInfo = (MapInfo) obj;
        return (readNormalizedMeasure == mapInfo.readNormalizedMeasure && readSatisfiedStatus == mapInfo.readSatisfiedStatus && targetObjectiveID.equals(mapInfo.targetObjectiveID) && writeNormalizedMeasure == mapInfo.writeNormalizedMeasure && writeSatisfiedStatus == mapInfo.writeSatisfiedStatus);
    }
