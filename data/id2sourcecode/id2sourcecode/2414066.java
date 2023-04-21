    public static double[] shrinkRegionStartEnd(double start, double end, double proportion, int alignment) {
        StatisticsToolkit.checkValueIncreasing(0, proportion, 1);
        double position = 0;
        switch(alignment) {
            case SwingConstants.LEFT:
                position = start;
                break;
            case SwingConstants.CENTER:
                position = (start + end) / 2;
                break;
            case SwingConstants.RIGHT:
                position = end;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return getRegionStartEnd(position, (end - start) * proportion, alignment);
    }
