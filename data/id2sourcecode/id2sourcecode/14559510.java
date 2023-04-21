    private double[] getPairwiseFlowImpl(int det1, int det2) {
        if (det1 == det2) {
            throw new IllegalArgumentException("det1 and det2 cannot be equal!");
        }
        if (this.calcInv) {
            if (buffer[det1] == null) {
                buffer[det1] = new double[this.getDataLayout().getChannelCount()][];
                double[] tempret = calculatePairwiseFlow(this.currentFrame, det1, det2);
                buffer[det1][det2] = tempret;
                return tempret;
            } else if (buffer[det1][det2] == null) {
                double[] tempret = calculatePairwiseFlow(this.currentFrame, det1, det2);
                buffer[det1][det2] = tempret;
                return tempret;
            } else {
                return buffer[det1][det2];
            }
        } else {
            boolean invert = false;
            int dMore, dLess;
            if (det1 > det2) {
                dMore = det1;
                dLess = det2;
            } else {
                dMore = det2;
                dLess = det1;
                invert = true;
            }
            if (buffer[dMore] == null) {
                buffer[dMore] = new double[dMore][];
                buffer[dMore][dLess] = calculatePairwiseFlow(this.currentFrame, dMore, dLess);
            } else if (buffer[dMore][dLess] == null) {
                buffer[dMore][dLess] = calculatePairwiseFlow(this.currentFrame, dMore, dLess);
            }
            if (invert) {
                return new double[] { -buffer[dMore][dLess][0], buffer[dMore][dLess][1] };
            } else {
                return buffer[dMore][dLess];
            }
        }
    }
