    public double[] estimateGroup(int start, int end) {
        double temp;
        lowGroup = Math.abs(getDiff(start));
        highGroup = lowGroup;
        double oldLowGroup = lowGroup;
        double oldHighGroup = highGroup;
        for (int i = start + 1; i <= end; i++) {
            temp = Math.abs(getDiff(i));
            lowGroup = Math.min(lowGroup, temp);
            highGroup = Math.max(highGroup, temp);
        }
        highGroup = (lowGroup + highGroup) / 2;
        double changes;
        int round = 0;
        do {
            oldLowGroup = lowGroup;
            oldHighGroup = highGroup;
            double dLow, dHigh, dMid = 0;
            double sumLow = 0d;
            double sumHign = 0d;
            double totalLow = 0d;
            double totalMid = 0d;
            double totalHigh = 0d;
            for (int i = start; i <= end; i++) {
                temp = Math.abs(getDiff(i));
                dLow = Math.abs(temp - lowGroup);
                dHigh = Math.abs(temp - highGroup);
                if (dLow <= dHigh) {
                    sumLow += temp;
                    totalLow++;
                } else {
                    sumHign += temp;
                    totalHigh++;
                }
            }
            lowGroup = sumLow / totalLow;
            highGroup = sumHign / totalHigh;
            dLow = oldLowGroup - lowGroup;
            dHigh = oldHighGroup - highGroup;
            changes = Math.max(dLow * dLow, Math.max(dMid * dMid, dHigh * dHigh));
            round++;
        } while (changes > 0.001 && round < 10000);
        double[] result = new double[3];
        result[0] = lowGroup;
        result[2] = highGroup;
        return result;
    }
