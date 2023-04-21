    public void setZero() {
        final int TIMEOUT = 1000;
        final int DESIRED_DELTA = 3;
        final int INIT_MIN = 999;
        final int INIT_MAX = -999;
        final int MAX_TRIES = 6;
        int startTime;
        int reading;
        int minRead = INIT_MIN, maxRead = INIT_MAX;
        doWait(TIMEOUT);
        for (int i = 0; i < MAX_TRIES; i++) {
            startTime = (int) System.currentTimeMillis();
            for (; ; ) {
                if ((int) System.currentTimeMillis() - startTime > TIMEOUT) break;
                reading = getRawReading();
                doWait(1);
                if (reading < minRead) minRead = reading;
                if (reading > maxRead) maxRead = reading;
            }
            if (maxRead - minRead == DESIRED_DELTA) break;
            if (maxRead - minRead <= DESIRED_DELTA && i > MAX_TRIES / 2) break;
            minRead = INIT_MIN;
            maxRead = INIT_MAX;
        }
        if (minRead == INIT_MIN) return;
        zeroRangeMin = minRead;
        zeroRangeMax = maxRead;
        setZeroHeading();
        offset = (minRead + maxRead) / 2;
    }
