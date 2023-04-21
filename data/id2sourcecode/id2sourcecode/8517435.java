    public void init(long startTime, long endTime, String fld) {
        String endDate = NumberUtils.long2sortableStr(endTime);
        String startDate = NumberUtils.long2sortableStr(startTime);
        timeFilter = new BoundaryBoxFilter(fld, startDate, endDate, true, true);
        long middleTimeMili = (endTime + startTime) / 2;
        long radiumMili = Math.abs(endTime - startTime) / 2;
        timeDistanceBuilderFilter = new TimeDistanceBuilderFilter(fld, middleTimeMili, radiumMili);
    }
