    double calculateGStatistic(double lmean, double rmean) {
        double rlmean = (lmean + rmean) / 2;
        double lentropy;
        double rentropy;
        if (lmean > 0.1) lentropy = lmean * Math.log(lmean / rlmean); else lentropy = 0;
        if (rmean > 0.1) rentropy = rmean * Math.log(rmean / rlmean); else rentropy = 0;
        return 2 * (lentropy + rentropy);
    }
