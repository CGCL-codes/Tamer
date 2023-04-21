    protected ChromosomeLocus getLocusRecursive(double position, ChromosomeLocus[] array, int min, int max) {
        if (min == max) {
            return array[min];
        }
        int midL = (max + min) / 2;
        int midR = midL + 1;
        if (array[midL].getPosition() > position) {
            return getLocusRecursive(position, array, min, midL);
        }
        if (array[midR].getPosition() < position) {
            return getLocusRecursive(position, array, midR, max);
        }
        double distL = Math.abs(array[midL].getPosition() - position);
        double distR = Math.abs(array[midR].getPosition() - position);
        if (distL > distR) {
            return array[midR];
        } else {
            return array[midL];
        }
    }
