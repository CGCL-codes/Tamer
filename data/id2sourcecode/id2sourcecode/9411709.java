    public void setInterpolatedTime(final double time) {
        int iMin = 0;
        final StepInterpolator sMin = steps.get(iMin);
        double tMin = 0.5 * (sMin.getPreviousTime() + sMin.getCurrentTime());
        int iMax = steps.size() - 1;
        final StepInterpolator sMax = steps.get(iMax);
        double tMax = 0.5 * (sMax.getPreviousTime() + sMax.getCurrentTime());
        if (locatePoint(time, sMin) <= 0) {
            index = iMin;
            sMin.setInterpolatedTime(time);
            return;
        }
        if (locatePoint(time, sMax) >= 0) {
            index = iMax;
            sMax.setInterpolatedTime(time);
            return;
        }
        while (iMax - iMin > 5) {
            final StepInterpolator si = steps.get(index);
            final int location = locatePoint(time, si);
            if (location < 0) {
                iMax = index;
                tMax = 0.5 * (si.getPreviousTime() + si.getCurrentTime());
            } else if (location > 0) {
                iMin = index;
                tMin = 0.5 * (si.getPreviousTime() + si.getCurrentTime());
            } else {
                si.setInterpolatedTime(time);
                return;
            }
            final int iMed = (iMin + iMax) / 2;
            final StepInterpolator sMed = steps.get(iMed);
            final double tMed = 0.5 * (sMed.getPreviousTime() + sMed.getCurrentTime());
            if ((FastMath.abs(tMed - tMin) < 1e-6) || (FastMath.abs(tMax - tMed) < 1e-6)) {
                index = iMed;
            } else {
                final double d12 = tMax - tMed;
                final double d23 = tMed - tMin;
                final double d13 = tMax - tMin;
                final double dt1 = time - tMax;
                final double dt2 = time - tMed;
                final double dt3 = time - tMin;
                final double iLagrange = ((dt2 * dt3 * d23) * iMax - (dt1 * dt3 * d13) * iMed + (dt1 * dt2 * d12) * iMin) / (d12 * d23 * d13);
                index = (int) FastMath.rint(iLagrange);
            }
            final int low = FastMath.max(iMin + 1, (9 * iMin + iMax) / 10);
            final int high = FastMath.min(iMax - 1, (iMin + 9 * iMax) / 10);
            if (index < low) {
                index = low;
            } else if (index > high) {
                index = high;
            }
        }
        index = iMin;
        while ((index <= iMax) && (locatePoint(time, steps.get(index)) > 0)) {
            ++index;
        }
        steps.get(index).setInterpolatedTime(time);
    }
