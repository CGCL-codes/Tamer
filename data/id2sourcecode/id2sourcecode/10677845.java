    @Override
    public TestResults[] benchMarkTest(TestInfo tInfo, RealTimeValues rtValues, TestParams tParams) {
        int count = 0;
        if (timerAccuracySpecified()) {
            VerbosePrint.streamPrint(System.out, tParams, 1, "TimerConfig: Configuration test will not be run. Timer accuracy preconfigured as: ");
            VerbosePrint.streamPrintf(System.out, tParams, 1, "%.3f us" + VerbosePrint.getLineSeparator(), VerbosePrint.roundToMicroSec(getTimerAccuracy()));
            VerbosePrint.outPrintln(System.out, tParams, 1);
            return null;
        }
        double clock = ClockConfig.getClockAccuracy();
        int num = TestFinalDefinitions.TIMER_CONFIG_NSLEEP_TIME;
        int new_iters;
        boolean retryMsg = false;
        new_iters = updateIterations(tInfo, rtValues, tParams);
        TestResults[] testResult = establishSingleResult(TestId.SYSTIMER_CONFIG, tInfo.getTestIterations());
        VerbosePrint.testStart(System.out, tParams, name(), tInfo.getTestIterations());
        double savedAcceptablePercent = rtValues.getAcceptablePercent();
        double savedRangePercent = rtValues.getRangePercent();
        boolean flag = false;
        if (savedAcceptablePercent < 99.9 || savedRangePercent > 20.0) {
            flag = true;
        }
        double maximumPct = max(99.9, rtValues.getAcceptablePercent());
        rtValues.setAcceptablePercent(maximumPct);
        double minPct = min(20.0, rtValues.getRangePercent());
        rtValues.setRangePercent(minPct);
        if (flag) {
            VerbosePrint.outPrintln(System.out, tParams, 1);
            VerbosePrint.streamPrintln(System.out, tParams, 1, "TimerConfig: Altered the acceptable percentage to: " + rtValues.getAcceptablePercent());
            VerbosePrint.streamPrintln(System.out, tParams, 1, "TimerConfig: Altered the range percentage to: " + rtValues.getRangePercent());
            VerbosePrint.outPrintln(System.out, tParams, 1);
        }
        boolean testPass = false;
        int previousQuantum = 0;
        while (true) {
            boolean rc = false;
            if (count++ < tInfo.getThreshold()) {
                testResult[0].resetResults();
                rc = runTimerTest(num, testResult[0], tParams, new_iters);
                if (rc == false) {
                    return null;
                }
                if (testResult[0].getMedian() > clock) {
                    retryMsg = true;
                    if (testResult[0].checkStdDev(rtValues, tParams)) {
                        retryMsg = false;
                        testPass = true;
                        break;
                    }
                }
            } else {
                break;
            }
            if (retryMsg) {
                VerbosePrint.testRetry(System.out, tParams, name());
            }
            num = num * 2;
        }
        testResult[0].setWorkQuantum(num);
        if (testPass) {
            TestResults passedResult = null;
            int startQuantum = previousQuantum;
            int stopQuantum = num;
            int midQuantum = 0;
            passedResult = new TestResults(TestId.SYSTIMER_CONFIG, tInfo.getTestIterations(), true);
            passedResult.cloneValues(testResult[0]);
            TestResults tResult = new TestResults(TestId.SYSTIMER_CONFIG, tInfo.getTestIterations(), true);
            while (startQuantum <= stopQuantum) {
                tResult.resetResults();
                midQuantum = (startQuantum + stopQuantum) / 2;
                VerbosePrint.streamPrintln(System.out, tParams, 1, "TimerConfig: Refine Work Quantum. Range from: " + startQuantum + " to: " + stopQuantum);
                runTimerTest(midQuantum, tResult, tParams, new_iters);
                if (tResult.checkStdDev(rtValues, tParams)) {
                    passedResult.resetResults();
                    tResult.setWorkQuantum(midQuantum);
                    passedResult.cloneValues(tResult);
                    stopQuantum = midQuantum - 1;
                } else {
                    startQuantum = midQuantum + 1;
                }
            }
            testResult[0] = passedResult;
        }
        VerbosePrint.streamPrintln(System.out, tParams, 1, "TimerConfig: WorkQuantum = " + testResult[0].getWorkQuantum());
        VerbosePrint.streamPrint(System.out, tParams, 1, "TimerConfig: Benchmark will be configured " + "to expect the timer accuracy to be accurate to ");
        VerbosePrint.streamPrintf(System.out, tParams, 1, "%.3f us" + VerbosePrint.getLineSeparator(), VerbosePrint.roundToMicroSec(testResult[0].getMedian()));
        VerbosePrint.outPrintln(System.out, tParams, 1);
        if (flag) {
            rtValues.setAcceptablePercent(savedAcceptablePercent);
            rtValues.setRangePercent(savedRangePercent);
        }
        return testResult;
    }
