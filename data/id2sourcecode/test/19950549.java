    public double computePerfectMatchingThreshold() {
        Collections.sort(this.weights);
        if (!this.hasPerfectMatching()) {
            return -1;
        }
        int minimumValidIndex = this.weights.size() - 1;
        if (this.filterByMaximum(this.weights.get(0)).hasPerfectMatching()) {
            return this.weights.get(0);
        }
        int maximumInvalidIndex = 0;
        int currentIndex = 0;
        double currentWeight = 0;
        while (minimumValidIndex > maximumInvalidIndex + 1) {
            currentIndex = (maximumInvalidIndex + minimumValidIndex) / 2;
            currentWeight = this.weights.get(currentIndex);
            if (this.filterByMaximum(currentWeight).hasPerfectMatching()) {
                minimumValidIndex = currentIndex;
            } else {
                maximumInvalidIndex = currentIndex;
            }
        }
        return this.weights.get(minimumValidIndex);
    }
