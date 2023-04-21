    public static String getRelationData(String id) {
        long low = 0;
        long relationIndexFileSize = getRelationIndexFileSize();
        long high = relationIndexFileSize;
        while (low <= high) {
            long mid = (low + high) / 2;
            if (relationIndexFileSize - 1 <= mid) {
                return null;
            }
            long relationDataFP = getRelationDataFp(mid * 10);
            if (relationDataFP == -1) {
                return null;
            }
            String relationData = getRelationData(relationDataFP);
            if (relationData == null) {
                return null;
            }
            String[] lines = relationData.split("\t");
            String searchedID = lines[0];
            if (searchedID.compareTo(id) == 0) {
                return relationData;
            } else if (0 < searchedID.compareTo(id)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return null;
    }
