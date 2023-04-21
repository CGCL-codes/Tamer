    private int getWantedActivityVectorCount(int section, int sections, int maxActivityVectors, int lastCount, int increaseTill, int decreaseFrom, int[] startActivityCounts, int[] stopActivityCounts) {
        int count;
        if (section <= increaseTill) {
            count = startActivityCounts[section];
        } else if (section == decreaseFrom) {
            int firstStop = stopActivityCounts[section - decreaseFrom];
            int c = (lastCount + firstStop) / 2;
            while ((c == lastCount || c == firstStop) && c < maxActivityVectors) {
                c++;
            }
            count = c;
        } else if (section >= decreaseFrom + 1) {
            count = stopActivityCounts[section - decreaseFrom - 1];
        } else {
            int min = Math.min(maxActivityVectors, minActivityCount);
            int num;
            do {
                num = min + random.nextInt(maxActivityVectors - min + 1);
            } while (Math.abs(num - lastCount) > maxActivityChangeCount || (num == lastCount && random.nextFloat() >= 0.1f));
            count = num;
        }
        return count;
    }
