    private HashSet<CollectData> normalizeCollects(HashSet<CollectData> collects, int eid) {
        HashSet<CollectData> result = new HashSet<CollectData>();
        for (CollectData c : collects) {
            if (c.getEid() == eid) {
                result.add(c);
            } else {
                result.add(new CollectData(c.getPid(), eid, new TimestampValuePair(0, new byte[0]), new HashSet<TimestampValuePair>()));
            }
        }
        for (CollectData c : result) {
            for (TimestampValuePair rv : c.getWriteSet()) {
                if (rv.getValue() != null && rv.getValue().length > 0) rv.setHashedValue(md.digest(rv.getValue())); else rv.setHashedValue(new byte[0]);
            }
        }
        return result;
    }
