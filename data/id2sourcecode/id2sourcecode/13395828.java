    void calibrationProcess() {
        double muC = mu;
        double muL = mu;
        double muR = mu;
        double errC = 0.0;
        double errL = Double.NEGATIVE_INFINITY;
        double errR = Double.NEGATIVE_INFINITY;
        runModel();
        errC = calibrationError();
        System.out.println("Mu: " + df.format(mu) + "\tError: " + df.format(errC));
        while (errL < errC) {
            mu = muL = mu / 2;
            setupECij();
            runModel();
            errL = calibrationError();
            System.out.println("Mu inf: " + df.format(mu) + "\tError: " + df.format(errL));
            if (errL < errC) {
                errC = errL;
                errL = Double.NEGATIVE_INFINITY;
                muR = muC;
                errR = errC;
                mu = muC = muL;
            }
        }
        mu = muR;
        while (errR < errC) {
            mu = muR = 2 * mu;
            setupECij();
            runModel();
            errR = calibrationError();
            System.out.println("Mu sup: " + df.format(mu) + "\tError: " + df.format(errR));
        }
        while (Math.min(errR - errC, errL - errC) / errC > threshold3) {
            mu = (muC + muL) / 2;
            setupECij();
            runModel();
            double err = calibrationError();
            System.out.println("Mu : " + df.format(mu) + "\tError: " + df.format(err));
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
            setupECij();
            runModel();
            err = calibrationError();
            System.out.println("Mu : " + df.format(mu) + "\tError: " + df.format(err));
            if (err < errC) {
                muL = muC;
                errL = errC;
                muC = mu;
                errC = err;
            } else if (err < errR) {
                muR = mu;
                errR = err;
            }
        }
        System.out.println("Final value : " + muC);
    }
