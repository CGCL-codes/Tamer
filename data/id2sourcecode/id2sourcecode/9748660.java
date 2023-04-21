    int findGT(float angle) {
        int min = 0;
        int max = count;
        while (min != max) {
            int mid = (min + max) / 2;
            float midAngle = angles[mid];
            if (midAngle <= angle) min = mid + 1; else max = mid;
        }
        return min;
    }
