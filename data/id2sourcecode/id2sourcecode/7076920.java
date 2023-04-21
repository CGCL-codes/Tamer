    private long upAlteringTick(String n, long inTick) throws CalException {
        long[] period = new long[2];
        long finerTick, t0, lowerBound, upperBound, estimatedResult;
        double[] delta = new double[2];
        double e_y;
        String finerName;
        try {
            finerTick = operand1.convertUpFrom(n, inTick);
            finerName = operand1.getName();
            t0 = last(finerName, 0);
            estimateBound(finerName, period, delta);
            e_y = (double) period[1] / period[0];
            estimatedResult = (finerTick - t0 > 0) ? (long) ((finerTick - t0) / e_y + 0.5) : (long) ((finerTick - t0) / e_y - 0.5);
            delta[0] /= e_y;
            delta[1] /= e_y;
            lowerBound = (delta[0] < -period[0]) ? -period[0] : (long) delta[0] - 1;
            upperBound = (delta[1] > period[0]) ? period[0] : (long) delta[1] + 1;
            lowerBound += estimatedResult;
            upperBound += estimatedResult;
            if (statistics < upperBound - lowerBound) statistics = upperBound - lowerBound;
            long l, u;
            while (lowerBound < upperBound - 1) {
                l = first(finerName, estimatedResult);
                if (l > finerTick) {
                    upperBound = estimatedResult;
                    estimatedResult = (lowerBound + upperBound) / 2;
                    continue;
                }
                u = last(finerName, estimatedResult);
                if (u < finerTick) {
                    lowerBound = estimatedResult;
                    estimatedResult = (lowerBound + upperBound) / 2;
                    continue;
                }
                break;
            }
            if (last(finerName, estimatedResult) < finerTick) ++estimatedResult;
        } catch (UndefinedException e) {
            throw new CalException();
        }
        return (estimatedResult);
    }
