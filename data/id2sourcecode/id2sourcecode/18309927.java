    private float converge(int startVal, int expectedEndVal, int numYears) {
        int TOLERANCE = 2;
        float LOW_RATE = 0.0f;
        float HIGH_RATE = 0.1f;
        float rate = (LOW_RATE + HIGH_RATE) / 2;
        float prevLowRate = LOW_RATE;
        float prevHighRate = HIGH_RATE;
        int computedEndVal;
        boolean done = false;
        boolean isNeg = false;
        if (startVal > expectedEndVal) {
            isNeg = true;
            int temp = startVal;
            startVal = expectedEndVal;
            expectedEndVal = temp;
        }
        while (!done) {
            computedEndVal = startVal;
            for (int i = 0; i < numYears; i++) {
                computedEndVal += computedEndVal * rate;
            }
            if (Math.abs(computedEndVal - expectedEndVal) > TOLERANCE) {
                if (computedEndVal > expectedEndVal) {
                    prevHighRate = rate;
                    rate = (prevLowRate + rate) / 2;
                } else {
                    prevLowRate = rate;
                    rate = (rate + prevHighRate) / 2;
                }
            } else {
                done = true;
            }
        }
        if (isNeg) {
            rate *= -1.0f;
        }
        return rate;
    }
