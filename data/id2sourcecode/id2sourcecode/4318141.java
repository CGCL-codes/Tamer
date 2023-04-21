    public double estimateFromNodeUniform(int symbolID, int nodeIndex, double uniformEstimate) {
        if (symbolID < 0) return Double.NaN;
        double backoffAccumulator = 0.0;
        for (int currentNodeIndex = nodeIndex; currentNodeIndex >= 0; currentNodeIndex = _nodeBackoff[currentNodeIndex]) {
            int low = _nodeFirstOutcome[currentNodeIndex];
            int high = _nodeFirstOutcome[currentNodeIndex + 1] - 1;
            while (low <= high) {
                int mid = (high + low) / 2;
                if (_outcomeSymbol[mid] == symbolID) return backoffAccumulator + _outcomeLogEstimate[mid]; else if (_outcomeSymbol[mid] < symbolID) low = (low == mid ? mid + 1 : mid); else high = (high == mid ? mid - 1 : mid);
            }
            backoffAccumulator += _nodeLogOneMinusLambda[currentNodeIndex];
        }
        return (backoffAccumulator + uniformEstimate);
    }
