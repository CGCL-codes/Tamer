    public void runAlgorithm() {
        if (Flags.ALGO_PROGRESS) {
            System.out.println("Progress: Transshipment algorithm was started.");
            System.out.flush();
        }
        fireEvent("Transshipment algorithm started.");
        if (Flags.MEL) {
            System.out.println("Eingabe: ");
            System.out.println("Network: " + network);
            System.out.println("Edge capacities:" + edgeCapacities);
            System.out.println("Supplies: " + supplies);
        }
        if (GraphInstanceChecker.emptySupplies(network, supplies)) {
            if (Flags.MEL) System.out.println("No individuals - no flow.");
            resultFlowPathBased = new PathBasedFlowOverTime();
            return;
        }
        int upperBound;
        upperBound = TransshipmentBoundEstimator.calculateBound(network, transitTimes, edgeCapacities, supplies);
        if (Flags.ALGO_PROGRESS) {
            System.out.println("Progress: The upper bound for the time horizon was calculated.");
        }
        if (Flags.TRANSSHIPMENT_SHORT) {
            System.out.println("Upper bound for time horizon: " + (upperBound - 1));
        }
        int left = 1, right = upperBound;
        PathBasedFlowOverTime transshipmentWithoutTimeHorizon = null;
        if (Flags.ALGO_PROGRESS) {
            System.out.println("Progress: Now testing time horizon 1.");
        }
        fireEvent("Upper bound for the time horizon was calculated. Now testing time horizon 1.");
        PathBasedFlowOverTime dynamicTransshipment = useTransshipmentAlgorithm(network, transitTimes, edgeCapacities, nodeCapacities, supplies, 1, standardTHTAlgorithm);
        boolean found = false;
        int nonFeasibleT = 0;
        int feasibleT = -1;
        if (dynamicTransshipment == null) nonFeasibleT = 1; else {
            nonFeasibleT = 0;
            feasibleT = 1;
            found = true;
        }
        while (!found) {
            int testTimeHorizon = (nonFeasibleT * 2);
            if (testTimeHorizon >= upperBound) {
                feasibleT = upperBound;
                found = true;
            } else {
                if (Flags.ALGO_PROGRESS) {
                    System.out.println("Progress: Now testing time horizon " + testTimeHorizon + ".");
                }
                fireEvent("Now testing time horizon " + testTimeHorizon + ".");
                System.out.println(System.currentTimeMillis() + " ms");
                dynamicTransshipment = useTransshipmentAlgorithm(network, transitTimes, edgeCapacities, nodeCapacities, supplies, testTimeHorizon, standardTHTAlgorithm);
                if (dynamicTransshipment == null) nonFeasibleT = testTimeHorizon; else {
                    feasibleT = testTimeHorizon;
                    found = true;
                }
            }
        }
        left = nonFeasibleT;
        right = Math.min(feasibleT + 1, upperBound);
        do {
            int testTimeHorizon = (left + right) / 2;
            if (Flags.ALGO_PROGRESS) {
                System.out.println("Progress: Now testing time horizon " + testTimeHorizon + ".");
            }
            fireEvent("Now testing time horizon " + testTimeHorizon + ".");
            dynamicTransshipment = useTransshipmentAlgorithm(network, transitTimes, edgeCapacities, nodeCapacities, supplies, testTimeHorizon, standardTHTAlgorithm);
            if (dynamicTransshipment == null) left = testTimeHorizon; else {
                right = testTimeHorizon;
                transshipmentWithoutTimeHorizon = dynamicTransshipment;
            }
        } while (left < right - 1);
        if (left == right - 1 && transshipmentWithoutTimeHorizon != null) {
            if (Flags.ALGO_PROGRESS) {
                System.out.println("Progress: Transshipment algorithm has finished. Time horizon: " + right);
            }
            fireEvent("Solution found. The optimal time horizon is: " + right + " (estimated upper bound: " + (upperBound - 1) + ")");
            if (Flags.TRANSSHIPMENT_SHORT) {
                System.out.println("The optimal time horizon is: " + right + " (estimated upper bound: " + (upperBound - 1) + ")");
            }
            if (Flags.TRANSSHIPMENT_LONG) {
                System.out.println("A transshipment with time horizon (" + (upperBound - 1) + ")" + +right + ": ");
                System.out.println(transshipmentWithoutTimeHorizon);
            }
            if (Flags.TRANSSHIPMENT_RESULT_FLOW) {
                System.out.println(transshipmentWithoutTimeHorizon);
            }
        } else {
            fireEvent("No solution found.");
            if (Flags.TRANSSHIPMENT_SHORT) {
                System.out.println("No solution found.");
            }
            if (Flags.ALGO_PROGRESS) {
                System.out.println("Progress: Transshipment algorithm has finished. No solution.");
            }
            throw new AssertionError("No solution found. Upper bound wrong?");
        }
        if (left == right - 1 && transshipmentWithoutTimeHorizon != null) {
            if (additionalTHTAlgorithm != null && additionalTHTAlgorithm != standardTHTAlgorithm) {
                transshipmentWithoutTimeHorizon = useTransshipmentAlgorithm(network, transitTimes, edgeCapacities, nodeCapacities, supplies, right, additionalTHTAlgorithm);
                if (Flags.TRANSSHIPMENT_SHORT) {
                    System.out.println("Additional run with additional transshipment algorithm has finished.");
                }
                if (Flags.ALGO_PROGRESS) {
                    System.out.println("Progress: Additional transshipment algorithm has finished and the new solution was set.");
                }
                fireProgressEvent(100, "Run with additional transshipment algorithm has finished. The new solution was set.");
            }
        }
        resultFlowPathBased = transshipmentWithoutTimeHorizon;
    }
