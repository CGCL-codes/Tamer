    public void testProcessPriceSignal() {
        double minPriceBaseLoad = FirstPriceSignalMaintainingAlgorithm.getMinPriceBaseLoad();
        double maxPriceBaseLoad = FirstPriceSignalMaintainingAlgorithm.getMaxPriceBaseLoad();
        double correctiveStepFactor = FirstPriceSignalMaintainingAlgorithm.getCorrectiveStepFactor();
        double valueBelowMaxBaseLoad = (minPriceBaseLoad + maxPriceBaseLoad) / 2;
        double newPriceSignal;
        double newMinimumPriceSingal;
        double[] newSignalValues;
        newSignalValues = FirstPriceSignalMaintainingAlgorithm.processPriceSignal(valueBelowMaxBaseLoad, valueBelowMaxBaseLoad, 0.0);
        newPriceSignal = newSignalValues[0];
        newMinimumPriceSingal = newSignalValues[1];
        assertEquals(valueBelowMaxBaseLoad, newPriceSignal);
        assertEquals(0.0, newMinimumPriceSingal);
        newSignalValues = FirstPriceSignalMaintainingAlgorithm.processPriceSignal(maxPriceBaseLoad + 1, maxPriceBaseLoad + 2, 0.0);
        newPriceSignal = newSignalValues[0];
        newMinimumPriceSingal = newSignalValues[1];
        assertEquals((maxPriceBaseLoad + 2) * correctiveStepFactor, newPriceSignal);
        assertEquals((maxPriceBaseLoad + 2) * correctiveStepFactor, newMinimumPriceSingal);
        newSignalValues = FirstPriceSignalMaintainingAlgorithm.processPriceSignal(maxPriceBaseLoad + 1, valueBelowMaxBaseLoad, 0.0);
        newPriceSignal = newSignalValues[0];
        newMinimumPriceSingal = newSignalValues[1];
        assertEquals(maxPriceBaseLoad + 1, newPriceSignal);
        assertEquals(maxPriceBaseLoad + 1, newMinimumPriceSingal);
        newSignalValues = FirstPriceSignalMaintainingAlgorithm.processPriceSignal(valueBelowMaxBaseLoad, maxPriceBaseLoad + 1.0, maxPriceBaseLoad + 1.0);
        newPriceSignal = newSignalValues[0];
        newMinimumPriceSingal = newSignalValues[1];
        assertEquals(maxPriceBaseLoad + 1, newPriceSignal);
        assertEquals(maxPriceBaseLoad + 1, newMinimumPriceSingal);
    }
