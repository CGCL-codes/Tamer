    public static int getInsertIndex(Object[] objects, Object toInsert, boolean ascending) {
        if (objects.length == 0) {
            return 0;
        }
        int low = 0;
        int hight = objects.length - 1;
        while (low != hight) {
            int middle = (low + hight) / 2;
            if (middle != low && middle != hight) {
                int compare = compare(toInsert, objects[middle], ascending);
                if (compare == 0) {
                    low = hight = middle;
                } else if (compare > 0) {
                    low = middle;
                } else {
                    hight = middle;
                }
            } else if (middle == low) {
                int compare = compare(toInsert, objects[middle], ascending);
                if (compare <= 0) {
                    hight = low;
                } else {
                    low++;
                }
            } else if (middle == hight) {
                int compare = compare(objects[middle], toInsert, ascending);
                if (compare >= 0) {
                    low = hight;
                } else {
                    hight--;
                }
            }
        }
        int result = low;
        if (compare(toInsert, objects[low], ascending) > 0) {
            result++;
        }
        return result;
    }
