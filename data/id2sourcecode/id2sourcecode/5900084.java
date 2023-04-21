    private final int findInsertionPoint(int pIndexI, int nPointsI, TimeRange refI, java.util.Vector refStoreI, java.util.Vector otherStoreI) {
        int low = 0, high = refStoreI.size() - 1;
        double ptime = refI.getPointTime(pIndexI, nPointsI), erange;
        for (int idx = (high + low) / 2; (high >= low); idx = (high + low) / 2) {
            TimeRange tRange = (TimeRange) refStoreI.elementAt(idx);
            boolean greater = (tRange.getDuration() == 0.);
            if (ptime < tRange.getTime()) {
                high = idx - 1;
            } else if ((ptime > (erange = (tRange.getPtimes()[tRange.getNptimes() - 1] + tRange.getDuration()))) || (!greater && (ptime == erange))) {
                low = idx + 1;
            } else {
                int nPoints = ((pointsPerRange != null) ? ((Integer) pointsPerRange.elementAt(idx)).intValue() : tRange.getNptimes());
                if (nPoints == 1) {
                    low = idx + 1;
                    continue;
                }
                TimeRange other = ((otherStoreI == null) ? null : (TimeRange) otherStoreI.elementAt(idx));
                int lPoint, nPoints2;
                if (tRange.getNptimes() == 1) {
                    double value = ptime - tRange.getTime();
                    lPoint = (int) (value * nPoints / tRange.getDuration());
                } else {
                    int low2 = 0, high2 = tRange.getNptimes() - 1;
                    for (int idx1 = (low2 + high2) / 2; low2 <= high2; idx1 = (low2 + high2) / 2) {
                        double ttime = tRange.getPtimes()[idx1];
                        if (ptime < ttime) {
                            high2 = idx1 - 1;
                        } else {
                            low2 = idx1 + 1;
                        }
                    }
                    lPoint = high2;
                }
                nPoints2 = lPoint + 1;
                if (nPoints2 < nPoints) {
                    numberInArray -= (nPoints - nPoints2);
                    TimeRange ref2, other2, ref3 = null, other3 = null;
                    if (tRange.getNptimes() == 1) {
                        ref2 = new TimeRange(tRange.getTime(), nPoints2 * tRange.getDuration() / nPoints);
                        ref3 = new TimeRange(tRange.getPointTime(nPoints2, nPoints), tRange.getDuration() * (nPoints - nPoints2) / nPoints);
                    } else {
                        double[] times = new double[nPoints2];
                        System.arraycopy(tRange.getPtimes(), 0, times, 0, nPoints2);
                        ref2 = new TimeRange(times, tRange.getDuration());
                        times = new double[nPoints - nPoints2];
                        System.arraycopy(tRange.getPtimes(), nPoints2, times, 0, times.length);
                        ref3 = new TimeRange(times, tRange.getDuration());
                    }
                    refStoreI.setElementAt(ref2, idx);
                    if (other != null) {
                        if (other.getNptimes() == 1) {
                            other2 = new TimeRange(other.getTime(), nPoints2 * other.getDuration() / nPoints);
                            other3 = new TimeRange(other.getPointTime(nPoints2, nPoints), other.getDuration() * (nPoints - nPoints2) / nPoints);
                        } else {
                            double[] times = new double[nPoints2];
                            System.arraycopy(other.getPtimes(), 0, times, 0, nPoints2);
                            other2 = new TimeRange(times, other.getDuration());
                            times = new double[nPoints - nPoints2];
                            System.arraycopy(other.getPtimes(), nPoints2, times, 0, times.length);
                            other3 = new TimeRange(times, other.getDuration());
                        }
                        otherStoreI.setElementAt(other2, idx);
                    }
                    if (pointsPerRange != null) {
                        pointsPerRange.setElementAt(new Integer(nPoints2), idx);
                    }
                    if (nPoints2 < nPoints) {
                        insertConsecutive(0, nPoints - nPoints2, nPoints - nPoints2, null, ref3, other3, idx + 1, refStoreI, otherStoreI);
                    }
                }
                low = idx + 1;
                break;
            }
        }
        return (low);
    }
