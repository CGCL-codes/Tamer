    public Schedule assignRandomChargingSlotInChargingInterval(PolynomialFunction func, double startTime, double endTime, double joulesInInterval, double bit, Schedule chargingInParkingInterval) throws Exception {
        boolean notFound = true;
        boolean run = true;
        if (DecentralizedSmartCharger.debug) {
            System.out.println("assign random charging slot in interval");
            System.out.println(bit + "seconds between " + startTime + " - " + endTime);
            System.out.println("Schedule already:");
            chargingInParkingInterval.printSchedule();
        }
        double upper = endTime;
        double lower = startTime;
        double trial = (upper + lower) / 2;
        int countNotFoundInARow = 0;
        while (notFound) {
            run = true;
            upper = endTime;
            lower = startTime;
            trial = (upper + lower) / 2;
            double rand = Math.random();
            double integral;
            PolynomialFunction funcSubOpt = null;
            double fullSubOptIntegral = 0;
            while (run) {
                if (joulesInInterval >= 0) {
                    double err = Math.max(joulesInInterval / 100.0, 1.0);
                    if (DecentralizedSmartCharger.debug) {
                        System.out.println("integrate stat:" + startTime + " upto " + trial + " Function" + func.toString());
                        if (startTime == trial) {
                            System.out.println("TROUBLEt:");
                            System.out.println("error:" + err + " joules in interval" + joulesInInterval + " Function" + func.toString());
                        }
                    }
                    integral = DecentralizedSmartCharger.functionSimpsonIntegrator.integrate(func, startTime, trial);
                    if (integral < rand * joulesInInterval) {
                        lower = trial;
                        trial = (upper + lower) / 2;
                    } else {
                        upper = trial;
                        trial = (upper + lower) / 2;
                    }
                    if (Math.abs(integral - rand * joulesInInterval) <= err) {
                        run = false;
                    }
                } else {
                    if (funcSubOpt == null) {
                        funcSubOpt = turnSubOptimalSlotDistributionIntoProbDensityOfFindingAvailableSlot(func, startTime, endTime);
                    }
                    integral = DecentralizedSmartCharger.functionSimpsonIntegrator.integrate(funcSubOpt, startTime, trial);
                    if (fullSubOptIntegral == 0) {
                        fullSubOptIntegral = DecentralizedSmartCharger.functionSimpsonIntegrator.integrate(funcSubOpt, startTime, endTime);
                    }
                    double err = Math.max(Math.abs(fullSubOptIntegral) / 100.0, 1.0);
                    if (Math.abs(integral) < Math.abs(rand * fullSubOptIntegral)) {
                        lower = trial;
                        trial = (upper + lower) / 2;
                    } else {
                        upper = trial;
                        trial = (upper + lower) / 2;
                    }
                    if (Math.abs(Math.abs(integral) - Math.abs(rand * fullSubOptIntegral)) <= err) {
                        run = false;
                    }
                }
            }
            ChargingInterval c1;
            if (trial + bit > endTime) {
                c1 = null;
            } else {
                c1 = new ChargingInterval(trial, trial + bit);
            }
            if (c1 != null && chargingInParkingInterval.overlapWithTimeInterval(c1) == false) {
                notFound = false;
                countNotFoundInARow = 0;
                chargingInParkingInterval.addTimeInterval(c1);
            } else {
                countNotFoundInARow++;
                if (countNotFoundInARow > 100) {
                    chargingInParkingInterval = exitDistributionIfTooConstrained(startTime, endTime, bit, chargingInParkingInterval);
                    notFound = false;
                }
            }
        }
        chargingInParkingInterval.sort();
        return chargingInParkingInterval;
    }
