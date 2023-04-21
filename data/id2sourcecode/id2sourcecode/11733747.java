    public static int calculateBoundByStaticTransshipmentAndScaleFactorSearch(AbstractNetwork network, IdentifiableIntegerMapping<Edge> transitTimes, IdentifiableIntegerMapping<Edge> edgeCapacities, IdentifiableIntegerMapping<Node> supplies) {
        Node sink = null;
        IdentifiableIntegerMapping<Node> oneSupplies = new IdentifiableIntegerMapping<Node>(supplies.getDomainSize());
        LinkedList<Node> sources = new LinkedList<Node>();
        LinkedList<Node> sinks = new LinkedList<Node>();
        int maxSupply = 0;
        for (Node node : network.nodes()) {
            if (supplies.get(node) < 0) {
                if (sink != null) throw new AssertionError(GraphLocalization.getSingleton().getString("algo.graph.dynamicflow.OnlyOneSinkException"));
                if (sink == null) sink = node;
            }
            if (supplies.get(node) > 0) {
                oneSupplies.set(node, 1);
                sources.add(node);
                if (supplies.get(node) > maxSupply) maxSupply = supplies.get(node);
            }
        }
        oneSupplies.set(sink, -sources.size());
        sinks.add(sink);
        int upperBound = sources.size() + 1;
        int left = 1, right = upperBound;
        StaticTransshipment staticTransshipment = null;
        IdentifiableIntegerMapping<Edge> staticFlow = null;
        IdentifiableIntegerMapping<Edge> resultStaticFlow = null;
        staticTransshipment = new StaticTransshipment(network, edgeCapacities, oneSupplies);
        staticTransshipment.run();
        staticFlow = staticTransshipment.getFlow();
        boolean found = false;
        int nonFeasibleT = 0;
        int feasibleT = -1;
        if (staticFlow == null) nonFeasibleT = 1; else {
            nonFeasibleT = 0;
            feasibleT = 1;
            found = true;
        }
        while (!found) {
            int testScaleFactor = (nonFeasibleT * 2);
            if (testScaleFactor >= upperBound) {
                feasibleT = upperBound;
                found = true;
            } else {
                IdentifiableIntegerMapping<Edge> multipliedCapacities = new IdentifiableIntegerMapping<Edge>(edgeCapacities.getDomainSize());
                for (Edge edge : network.edges()) {
                    int eCap = edgeCapacities.get(edge);
                    if (eCap >= Math.floor(Integer.MAX_VALUE / testScaleFactor)) multipliedCapacities.set(edge, Integer.MAX_VALUE); else multipliedCapacities.set(edge, edgeCapacities.get(edge) * testScaleFactor);
                }
                staticTransshipment = new StaticTransshipment(network, multipliedCapacities, oneSupplies);
                staticTransshipment.run();
                staticFlow = staticTransshipment.getFlow();
                if (staticFlow == null) nonFeasibleT = testScaleFactor; else {
                    feasibleT = testScaleFactor;
                    found = true;
                }
            }
        }
        left = nonFeasibleT;
        right = Math.min(feasibleT + 1, upperBound);
        do {
            int testScaleFactor = (left + right) / 2;
            IdentifiableIntegerMapping<Edge> multipliedCapacities = new IdentifiableIntegerMapping<Edge>(edgeCapacities.getDomainSize());
            for (Edge edge : network.edges()) {
                int eCap = edgeCapacities.get(edge);
                if (eCap >= Math.floor(Integer.MAX_VALUE / testScaleFactor)) multipliedCapacities.set(edge, Integer.MAX_VALUE); else multipliedCapacities.set(edge, edgeCapacities.get(edge) * testScaleFactor);
            }
            staticTransshipment = new StaticTransshipment(network, multipliedCapacities, oneSupplies);
            staticTransshipment.run();
            staticFlow = staticTransshipment.getFlow();
            if (staticFlow == null) left = testScaleFactor; else {
                right = testScaleFactor;
                resultStaticFlow = staticFlow;
            }
        } while (left < right - 1);
        if (left == right - 1 && resultStaticFlow != null) {
            PathBasedFlow pathFlows = PathDecomposition.calculatePathDecomposition(network, supplies, sources, sinks, staticFlow);
            int maxLength = 0;
            for (StaticPathFlow staticPathFlow : pathFlows) {
                int length = 0;
                for (Edge edge : staticPathFlow) {
                    length += transitTimes.get(edge);
                }
                if (length > maxLength) maxLength = length;
            }
            if (Flags.BOUND_ESTIMATOR_LONG) {
                System.out.println("Path decomposition: " + pathFlows);
            }
            if (Flags.BOUND_ESTIMATOR) {
                System.out.println();
                System.out.println("Max Length: " + maxLength + " Max Supply: " + maxSupply + " Sum:" + (maxLength + maxSupply) * sources.size());
                System.out.println();
            }
            return ((maxLength + maxSupply) * right + 1);
        }
        throw new AssertionError("Binary search found no working testScaleFactor.");
    }
