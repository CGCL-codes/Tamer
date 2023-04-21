        public void setStartEndValue(double start, double end) {
            StatisticsToolkit.checkValueIncreasing(start, end);
            this.center = (start + end) / 2;
            this.length = (end - start);
        }
