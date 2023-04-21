    public static String getTreeData(boolean isSpecial, String id) {
        long low = 0;
        long treeIndexFileSize = getTreeIndexFileSize(isSpecial);
        long high = treeIndexFileSize;
        while (low <= high) {
            long mid = (low + high) / 2;
            if (treeIndexFileSize - 1 <= mid) {
                return null;
            }
            long treeDataFP = getTreeDataFp(mid * 10, isSpecial);
            if (treeDataFP == -1) {
                return null;
            }
            String treeData = getTreeData(treeDataFP, isSpecial);
            if (treeData == null) {
                return null;
            }
            String[] lines = treeData.split("\t");
            String searchedID = lines[0];
            if (searchedID.compareTo(id) == 0) {
                return treeData;
            } else if (0 < searchedID.compareTo(id)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return null;
    }
