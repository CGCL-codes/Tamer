    public double interpolate(double fraction) {
        int low = 1;
        int high = f_lengths.size() - 1;
        int mid = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            if (fraction > f_lengths.get(mid).getFraction()) low = mid + 1; else if (mid > 0 && fraction < f_lengths.get(mid - 1).getFraction()) high = mid - 1; else {
                break;
            }
        }
        final LengthItem prevItem = f_lengths.get(mid - 1);
        final double prevFraction = prevItem.getFraction();
        final double prevT = prevItem.getT();
        final LengthItem item = f_lengths.get(mid);
        final double proportion = (fraction - prevFraction) / (item.getFraction() - prevFraction);
        final double interpolatedT = prevT + (proportion * (item.getT() - prevT));
        return getY(interpolatedT);
    }
