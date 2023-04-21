    public void runCalibrationProcess() {
        double muC = mu;
        double muL = mu;
        double muR = mu;
        double errC = 0.0;
        double errL = Double.NEGATIVE_INFINITY;
        double errR = Double.NEGATIVE_INFINITY;
        runModel();
        errC = calib.error(trips, X);
        log.info("PLCM:\tCalibration loop. Initial condition run completed");
        calibrationLoopInfo(mu, errC);
        while (errL < errC) {
            mu = muL = mu / 2;
            runModel();
            errL = calib.error(trips, X);
            calibrationLoopInfo(mu, errL);
            if (errL < errC) {
                errR = errC;
                errC = errL;
                errL = Double.NEGATIVE_INFINITY;
                muR = muC;
                mu = muC = muL;
            }
        }
        mu = muR;
        while (errR < errC) {
            mu = muR = 2 * mu;
            runModel();
            errR = calib.error(trips, X);
            calibrationLoopInfo(mu, errR);
        }
        double omuL = muL;
        double omuC = muC;
        double omuR = muR;
        int count = 0;
        while ((count++ < maxiter) && (Math.min(errR - errC, errL - errC) / errC > threshold3)) {
            mu = (muC + muL) / 2;
            runModel();
            double err = calib.error(trips, X);
            calibrationLoopInfo(mu, err);
            if (err < errC) {
                muR = muC;
                errR = errC;
                muC = mu;
                errC = err;
            } else if (err < errL) {
                muL = mu;
                errL = err;
            }
            mu = (muC + muR) / 2;
            runModel();
            err = calib.error(trips, X);
            calibrationLoopInfo(mu, err);
            if (err < errC) {
                muL = muC;
                errL = errC;
                muC = mu;
                errC = err;
            } else if (err < errR) {
                muR = mu;
                errR = err;
            }
            if ((omuL == muL) && (omuC == muC) && (omuR == muR)) {
                muC = (muL + muR) / 2 + (MatsimRandom.getRandom().nextDouble() - 0.5) * (muR - muL) / 10;
            }
            omuL = muL;
            omuC = muC;
            omuR = muR;
        }
    }
