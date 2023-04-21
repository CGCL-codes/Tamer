    private int absIndex(double abscisse, int startIdx, int endIdx) {
        if (startIdx == endIdx) {
            return startIdx;
        } else {
            int middleIdx = (startIdx + endIdx) / 2;
            double valStart = startIdx == 0 ? this.start : this.abscisses[startIdx - 1];
            double valMidd = this.abscisses[middleIdx - 1];
            if (abscisse >= valStart && abscisse < valMidd) {
                return this.absIndex(abscisse, startIdx, middleIdx);
            } else {
                return this.absIndex(abscisse, middleIdx, endIdx);
            }
        }
    }
