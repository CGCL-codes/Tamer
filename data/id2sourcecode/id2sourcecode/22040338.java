    protected final int getId(int feature, int currentLabel) {
        int num = lookup[feature];
        int min = 0;
        int max = num - 1;
        int mid = 0;
        boolean found = false;
        while (min + 10 <= max) {
            mid = (min + max) / 2;
            if (lookup[feature + 1 + mid * 2] < currentLabel) {
                min = mid + 1;
            } else if (lookup[feature + 1 + mid * 2] > currentLabel) {
                max = mid - 1;
            } else {
                found = true;
                break;
            }
        }
        if (!found) {
            mid = min;
            while (!found && mid <= max) {
                if (lookup[feature + 1 + mid * 2] == currentLabel) {
                    found = true;
                    break;
                }
                mid++;
            }
        }
        if (found) return lookup[feature + 1 + mid * 2 + 1];
        return lookup[feature + 1 + num * 2];
    }
