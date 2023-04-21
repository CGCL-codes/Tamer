    public static int findLiveItemsLowerBound(XYDataset dataset, int series, double xLow, double xHigh) {
        int itemCount = dataset.getItemCount(series);
        if (itemCount <= 1) {
            return 0;
        }
        if (dataset.getDomainOrder() == DomainOrder.ASCENDING) {
            int low = 0;
            int high = itemCount - 1;
            int mid = (low + high) / 2;
            double lowValue = dataset.getXValue(series, low);
            if (lowValue >= xLow) {
                return low;
            }
            double highValue = dataset.getXValue(series, high);
            if (highValue < xLow) {
                return high;
            }
            while (high - low > 1) {
                double midV = dataset.getXValue(series, mid);
                if (midV >= xLow) {
                    high = mid;
                } else {
                    low = mid;
                }
                mid = (low + high) / 2;
            }
            return mid;
        } else if (dataset.getDomainOrder() == DomainOrder.DESCENDING) {
            int low = 0;
            int high = itemCount - 1;
            int mid = (low + high) / 2;
            double lowValue = dataset.getXValue(series, low);
            if (lowValue <= xHigh) {
                return low;
            }
            double highValue = dataset.getXValue(series, high);
            if (highValue > xHigh) {
                return high;
            }
            while (high - low > 1) {
                double midV = dataset.getXValue(series, mid);
                if (midV > xHigh) {
                    low = mid;
                } else {
                    high = mid;
                }
                mid = (low + high) / 2;
            }
            return mid;
        } else {
            int index = 0;
            while (index < itemCount && dataset.getXValue(series, index) < xLow) {
                index++;
            }
            return Math.max(0, index - 1);
        }
    }
