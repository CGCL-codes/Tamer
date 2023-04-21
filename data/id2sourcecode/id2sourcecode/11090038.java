    public static int findLiveItemsUpperBound(XYDataset dataset, int series, double xLow, double xHigh) {
        int itemCount = dataset.getItemCount(series);
        if (itemCount <= 1) {
            return 0;
        }
        if (dataset.getDomainOrder() == DomainOrder.ASCENDING) {
            int low = 0;
            int high = itemCount - 1;
            int mid = (low + high + 1) / 2;
            double lowValue = dataset.getXValue(series, low);
            if (lowValue > xHigh) {
                return low;
            }
            double highValue = dataset.getXValue(series, high);
            if (highValue <= xHigh) {
                return high;
            }
            while (high - low > 1) {
                double midV = dataset.getXValue(series, mid);
                if (midV <= xHigh) {
                    low = mid;
                } else {
                    high = mid;
                }
                mid = (low + high + 1) / 2;
            }
            return mid;
        } else if (dataset.getDomainOrder() == DomainOrder.DESCENDING) {
            int low = 0;
            int high = itemCount - 1;
            int mid = (low + high) / 2;
            double lowValue = dataset.getXValue(series, low);
            if (lowValue < xLow) {
                return low;
            }
            double highValue = dataset.getXValue(series, high);
            if (highValue >= xLow) {
                return high;
            }
            while (high - low > 1) {
                double midV = dataset.getXValue(series, mid);
                if (midV >= xLow) {
                    low = mid;
                } else {
                    high = mid;
                }
                mid = (low + high) / 2;
            }
            return mid;
        } else {
            int index = itemCount - 1;
            while (index >= 0 && dataset.getXValue(series, index) > xHigh) {
                index--;
            }
            return Math.min(itemCount - 1, index + 1);
        }
    }
