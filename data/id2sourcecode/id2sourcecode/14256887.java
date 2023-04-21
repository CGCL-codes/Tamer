    public HistogramNode addValue(AdaptiveHistogram root, float value) {
        HistogramNode self = this;
        if (value >= minValue && value <= maxValue) {
            if (count < root.getCountPerNodeLimit()) {
                count++;
            } else {
                float splitValue = (minValue + maxValue) / 2;
                long rightCount = count / 2;
                long leftCount = rightCount;
                boolean countWasOdd = (leftCount + rightCount < count);
                if (value > splitValue) {
                    rightCount++;
                    leftCount += (countWasOdd ? 1 : 0);
                } else {
                    leftCount++;
                    rightCount += (countWasOdd ? 1 : 0);
                }
                HistogramNode leftNode = new HistogramDataNode(leftCount, minValue, splitValue);
                HistogramNode rightNode = new HistogramDataNode(rightCount, splitValue, maxValue);
                self = new HistogramForkNode(splitValue, leftNode, rightNode);
            }
        } else {
            if (count < root.getCountPerNodeLimit()) {
                count++;
                if (value < minValue) minValue = value;
                if (value > maxValue) maxValue = value;
            } else {
                if (value < minValue) {
                    minValue = Math.min(minValue, (value + maxValue) / 2);
                    self = new HistogramForkNode(minValue, new HistogramDataNode(1, value, minValue), this);
                } else {
                    maxValue = Math.max(maxValue, (minValue + value) / 2);
                    self = new HistogramForkNode(maxValue, this, new HistogramDataNode(1, maxValue, value));
                }
            }
        }
        return self;
    }
