    public final double determineNextReference(Rmap matchI, TimeRelativeRequest requestI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        double referenceR = Double.NaN;
        DataArray data = matchI.extract(getChannelName());
        if (data == null || data.getNumberOfPoints() == 0 || data.timeRanges == null) {
            referenceR = Double.NaN;
        } else {
            double[] times;
            TimeRange tr;
            switch(requestI.getRelationship()) {
                case TimeRelativeRequest.BEFORE:
                case TimeRelativeRequest.AT_OR_BEFORE:
                    referenceR = ((TimeRange) data.timeRanges.firstElement()).getTime();
                    break;
                case TimeRelativeRequest.AT_OR_AFTER:
                case TimeRelativeRequest.AFTER:
                    tr = (TimeRange) data.timeRanges.lastElement();
                    if (tr.getInclusive()) {
                        referenceR = tr.getPtimes()[tr.getNptimes() - 1] + tr.getDuration();
                    } else {
                        times = data.getTime();
                        referenceR = times[times.length - 1];
                    }
                    break;
            }
        }
        return (referenceR);
    }
