    private void calculateIntervals() {
        trackpoints = new ArrayList<TrackpointT>();
        ActivityListT activityList = trainingCenterDatabase.getActivities();
        Long lastTimeEpochUtc = null;
        boolean maxNotCalculated = true;
        boolean minNotCalculated = true;
        if (activityList != null) {
            for (ActivityT activity : activityList.getActivity()) {
                for (ActivityLapT lap : activity.getLap()) {
                    for (TrackT track : lap.getTrack()) {
                        trackCount++;
                        for (TrackpointT trackpoint : track.getTrackpoint()) {
                            trackpointCount++;
                            trackpoints.add(trackpoint);
                            Long thisTimestamp = trackpoint.getTime().normalize().toGregorianCalendar().getTimeInMillis();
                            if (lastTimeEpochUtc != null) {
                                Long diff = thisTimestamp - lastTimeEpochUtc;
                                avgTrackpointInterval = (avgTrackpointInterval + diff) / 2;
                                if (maxNotCalculated || diff > maxTrackpointInterval) {
                                    maxTrackpointInterval = diff;
                                    maxNotCalculated = false;
                                }
                                if (minNotCalculated || diff < minTrackpointInterval) {
                                    minTrackpointInterval = diff;
                                    minNotCalculated = false;
                                }
                            }
                            lastTimeEpochUtc = thisTimestamp;
                        }
                    }
                }
            }
        } else {
            trackpointCount = 0;
            minTrackpointInterval = 0L;
            maxTrackpointInterval = 0L;
            avgTrackpointInterval = 0L;
        }
    }
