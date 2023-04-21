    public boolean findUniformBW(boolean metErrorBound) throws IllegalActionException {
        director.resetStateElements();
        if (metErrorBound) {
            high = currentLength;
        } else {
            low = currentLength;
        }
        currentLength = (high + low) / 2;
        if (currentLength == high) {
            return true;
        }
        if (currentLength == low) {
            currentLength = high;
            return true;
        }
        setUniformValues();
        return false;
    }
