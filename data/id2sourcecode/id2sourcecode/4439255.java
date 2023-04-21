    private ListIterator<CoreKeyframe> getUpperBound(float time) {
        int lowerBound = 0;
        int upperBound = keyframes.size() - 1;
        while (lowerBound < upperBound - 1) {
            int middle = (lowerBound + upperBound) / 2;
            if (time >= keyframes.get(middle).getTime()) {
                lowerBound = middle;
            } else {
                upperBound = middle;
            }
        }
        return keyframes.listIterator(upperBound);
    }
