    protected Map<String, Long> analyze(MarkerSetHolder markerSetHolder) {
        MarkerSet markerSet = markerSetHolder.getMarkerSets().get(MarkerSetHolderEnum.word.name());
        if (markerSet == null) {
            markerSet = markerSetHolder.getMarkerSets().get(MarkerSetHolderEnum.phone.name());
        }
        Map<String, Long> map = new HashMap<String, Long>();
        Long minLength = null;
        Long maxLength = null;
        Long avgLength = null;
        Long minDistance = null;
        Long maxDistance = null;
        Long avgDistance = null;
        Marker previous = null;
        for (Marker m : markerSet.getMarkers()) {
            if (previous == null) {
                previous = m;
                avgLength = m.getLength();
                minLength = m.getLength();
                maxLength = m.getLength();
            } else {
                Long distance = m.getStart() - (previous.getStart() + previous.getLength());
                if (minDistance == null) {
                    minDistance = distance;
                    maxDistance = distance;
                    avgDistance = distance;
                }
                minDistance = Math.min(minDistance, distance);
                maxDistance = Math.max(maxDistance, distance);
                avgDistance = (avgDistance + distance) / 2;
            }
            minLength = Math.min(minLength, m.getLength());
            maxLength = Math.max(maxLength, m.getLength());
            avgLength = (avgLength + m.getLength()) / 2;
        }
        map.put(paramEnum.minTstLength.name(), minLength);
        map.put(paramEnum.maxTstLength.name(), maxLength);
        map.put(paramEnum.avgTstLength.name(), avgLength);
        map.put(paramEnum.minTstDistance.name(), minDistance);
        map.put(paramEnum.maxTstDistance.name(), maxDistance);
        map.put(paramEnum.avgTstDistance.name(), avgDistance);
        return map;
    }
